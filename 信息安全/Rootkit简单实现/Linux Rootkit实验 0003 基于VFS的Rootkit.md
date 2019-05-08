# 基于VFS的Rootkit

> Virtual file systems (VFS) are an abstraction layer to allow easy communication with other filesystems such as ext4, reiser fs, or other special filesystems like procfs. This extra layer translates easy to use VFS functions to their appropriate functions offered by the given filesystem. This allows a developer to interact solely with the VFS and not needing to find, handle, and support the different functions and types of individual filesystems. 
>
> 为什么VFS可以实现文件系统隐藏：
>
> 1. First, we can hook a VFS function and deal with that one function to hide information from the concrete filesystem.
> 2. procfs is a supported filesystem
>
> **Procfs (Proc filesystem)** 
>
> The proc filesystem is an interface to easily manage kernel data structures. This includes being able to retrieve and even change data inside the linux kernel at runtime. More importantly, for us, it also provides an interface for process data. Each process is mapped to procfs by its given process id number. Retrieving this pid number allows any tool to pull, with appropriate privileges, whatever data it needs to find out about that given process. This includes its memory mapping, memory usage, network usage, parameters, environment variables, and etc. Given this, if we know the pid and we're hooked into the VFS for procfs, we can also manipulate data returned to these tools to hide processes. 

```c
SYSCALL_DEFINE3(getdents, unsigned int, fd,
		struct linux_dirent __user *, dirent, unsigned int, count)
{
	struct fd f;
	struct linux_dirent __user * lastdirent;
	struct getdents_callback buf = {
		.ctx.actor = filldir,
		.count = count,
		.current_dir = dirent
	};
	int error;
	if (!access_ok(VERIFY_WRITE, dirent, count))
		return -EFAULT;
	f = fdget(fd);
	if (!f.file)
		return -EBADF;
    // 调用的是iterate_dir函数
	error = iterate_dir(f.file, &buf.ctx);
	if (error >= 0)
		error = buf.error;
	lastdirent = buf.previous;
	if (lastdirent) {
		if (put_user(buf.ctx.pos, &lastdirent->d_off))
			error = -EFAULT;
		else
			error = count - buf.count;
	}
	fdput(f);
	return error;
}
```

fs.h有iterate_dir函数

```c
struct dir_context {
	const filldir_t actor;
	loff_t pos;
};
int iterate_dir(struct file *file, struct dir_context *ctx)
{
	struct inode *inode = file_inode(file);
	int res = -ENOTDIR;
	if (!file->f_op->iterate)
		goto out;
	res = security_file_permission(file, MAY_READ);
	if (res)
		goto out;
	res = mutex_lock_killable(&inode->i_mutex);
	if (res)
		goto out;
	res = -ENOENT;
	if (!IS_DEADDIR(inode)) {
		ctx->pos = file->f_pos;
		res = file->f_op->iterate(file, ctx);
		file->f_pos = ctx->pos;
		file_accessed(file);
	}
	mutex_unlock(&inode->i_mutex);
out:
	return res;
}
EXPORT_SYMBOL(iterate_dir);
```

调用了file_operations里面的iterate函数，在VFS中有关file_operation定义如下:

```c
const struct file_operations ext4_dir_operations = {
	.llseek		= ext4_dir_llseek,
	.read		= generic_read_dir,
	.iterate	= ext4_readdir,
	.unlocked_ioctl = ext4_ioctl,
#ifdef CONFIG_COMPAT
	.compat_ioctl	= ext4_compat_ioctl,
#endif
	.fsync		= ext4_sync_file,
	.release	= ext4_release_dir,
};
```

可以看到，实现的是readdir

，中间过程比较复杂，简单描述如下：通过getdents系统调用了来获取当前目录下的文件时，file->f_op->readdir(file, buf, filler)调用的实际上是ext4_dir_operations函数集中的readdir()函数。即由ex4文件系统驱动来读取当前目录文件中的一个个目录项。 ext4_readdir最终会通过filldir把目录里面的项目一个一个的填到getdents返回的缓冲区里，缓冲区里是一个个的linux_dirent。

所以真正重要的是filler部分，在fs.h中可以找到:

```c
typedef  int (*filldir_t)(void *, const char *, int, loff_t, u64, unsigned);
```

总的来说，调用层次如下：

```c
sys_getdents-> iterate_dir-> struct file_operations.iterate->省略若干层次 -> struct dir_context.actor(mostly filldir)
```


要达到隐藏文件的目的，我们需要hooking filldir，在hooking function中去掉我们需要隐藏的文件记录，不填到缓冲区，这样应用程序就收不到相应的记录，也就打到了隐藏文件的目的。

```c
int fake_filldir(struct dir_context *ctx, const char *name, int namlen,
                loff_t offset, u64 ino, unsigned d_type)
{
        if (strncmp(name, SECRET_FILE, strlen(SECRET_FILE)) == 0) {
                printk("Hiding: %s", name);
                return 0;
        }

        return real_filldir(ctx, name, namlen, offset, ino, d_type);
}
```

附:VFS 层

```c
struct file_operations {
         struct module *owner;
         loff_t (*llseek) (struct file *, loff_t, int);
         ssize_t (*read) (struct file *, char __user *, size_t, loff_t *);
         ssize_t (*write) (struct file *, const char __user *, size_t, loff_t *);
         ssize_t (*aio_read) (struct kiocb *, const struct iovec *, unsigned long, loff_t);
         ssize_t (*aio_write) (struct kiocb *, const struct iovec *, unsigned long, loff_t);
         int (*readdir) (struct file *, void *, filldir_t);
         unsigned int (*poll) (struct file *, struct poll_table_struct *);
         long (*unlocked_ioctl) (struct file *, unsigned int, unsigned long);
         long (*compat_ioctl) (struct file *, unsigned int, unsigned long);
         int (*mmap) (struct file *, struct vm_area_struct *);
         int (*open) (struct inode *, struct file *);
         int (*flush) (struct file *, fl_owner_t id);
         int (*release) (struct inode *, struct file *);
         int (*fsync) (struct file *, loff_t, loff_t, int datasync);
         int (*aio_fsync) (struct kiocb *, int datasync);
         int (*fasync) (int, struct file *, int);
         int (*lock) (struct file *, int, struct file_lock *);
         ssize_t (*sendpage) (struct file *, struct page *, int, size_t, loff_t *, int);
         unsigned long (*get_unmapped_area)(struct file *, unsigned long, unsigned long, unsigned long, unsigned long);
         int (*check_flags)(int);
         int (*flock) (struct file *, int, struct file_lock *);
         ssize_t (*splice_write)(struct pipe_inode_info *, struct file *, loff_t *, size_t, unsigned int);
         ssize_t (*splice_read)(struct file *, loff_t *, struct pipe_inode_info *, size_t, unsigned int);
         int (*setlease)(struct file *, long, struct file_lock **);
         long (*fallocate)(struct file *file, int mode, loff_t offset, loff_t len);
};
```

**Readdir**

```c
struct linux_dirent {
               unsigned long  d_ino;     /* Inode number */
               unsigned long  d_off;     /* Offset to next linux_dirent */
               unsigned short d_reclen;  /* Length of this linux_dirent */
               char           d_name[];  /* Filename (null-terminated) */
                                   /* length is actually (d_reclen - 2 -
                                      offsetof(struct linux_dirent, d_name) */
               /*
               char           pad;       // Zero padding byte
               char           d_type;    // File type (only since Linux 2.6.4;
                                         // offset is (d_reclen - 1))
               */

           }
```

**fillter**

```c
dirent = buf->previous;
if (dirent) {
 if (__put_user(offset, &dirent->d_off))
         goto efault;
}
dirent = buf->current_dir;
if (__put_user(d_ino, &dirent->d_ino))
 goto efault;
if (__put_user(reclen, &dirent->d_reclen))
 goto efault;
if (copy_to_user(dirent->d_name, name, namlen))
 goto efault;
if (__put_user(0, dirent->d_name + namlen))
 goto efault;
if (__put_user(d_type, (char __user *) dirent + reclen - 1))
 goto efault;
```



