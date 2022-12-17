#include <linux/init.h> // __init __exit
#include <linux/module.h> // module_init module_exit
#include <linux/fs.h> // file_operations
#include <linux/uaccess.h> // copy_from_user copy_to_user
#include <linux/cdev.h> // cdev
#include <linux/device.h> // device_create
#include <linux/err.h> // IS_ERR
#include <linux/kernel.h> // printk
#include <linux/io.h> // ioremap
#include <linux/of.h> // of_find_node_by_path


/* 自定义设备结构体 */
struct simple_chr_dev {
	// 设备编号，高12位为主设备号，低20位为次设备号
	dev_t device_no = 0;
	// 主设备号：区分某一类的设备。不要自己定义，可能会和其他设备号冲突，建议由系统自动生成
	int device_major_no = 250;
	// 次设备号：用于区分同一类设备的不同个体或不同分区
	int device_minor_no;
	// 设备数量
	int number_of_devices = 1;
	// cdev是linux用来管理字符设备的结构体。cdev定义在include/linux/cdev.h中，实现在fs/char_dev.c中
	struct cdev *simple_cdev;
	// sysfs系统的设备类。是对于设备的高级抽象，可以让用户空间的程序根据自己要处理的事情来调用设备，而不是根据设备被接入到系统的方式或设备的工作原理来调用
	struct class *simple_dev_class;
	// 设备类
	struct device *simple_dev;
	// 设备节点类
	struct device_node *simple_dev_nd;
};

struct simple_chr_dev simple_chr_dev;

// 设备名称
const char device_name[] = "simple_chr_driver";
// 自定义class设备类名，会显示在/sys/class目录当中
const char device_class_name[] = "simple_chr_device_class";
// 设备节点名
const char device_node_name[] = "simple_chr_device_node";

/**
* 设备注册
* 完成后需要手动创建并关联节点，步骤如下：
* 1）使用lsmod或cat /proc/modules命令grep搜索查看模块的主设备号为xxx
* 2）创建设备节点mknod /dev/simple_chr_driver c xxx 0
* /proc/devices中的设备是驱动程序生成的，显示的是当前挂载在系统的模块。/dev下的设备是通过mknod加上去的，用户通过此设备名来访问驱动，是硬件模块的一个抽象封装。
*/
static int register_device(void)
{
	int result = 0;
	// printk函数是运行在内核态下的打印函数，而printf函数运行在用户态
	printk(KERN_INFO "simple_chr_driver: register_device() is called\n");
	// 注册字符设备，返回主设备号，在0到255之间。第一个参数传0表示设备号由系统自动生成。注册成功后，设备名出现在/proc/devices里
	result = register_chrdev(0, device_name, &simple_driver_fops);
	if (result < 0)
	{
		// 带有日志等级，还有比如KERN_DEBUG。等级高的被打印到控制台上，等级低的输出到日志文件中
		printk(KERN_WARNING "simple_chr_driver: can\'t register character device with errorcode = %i\n", result);
		return result;
	}
	simple_chr_dev.device_major_no = result;
	printk(KERN_INFO "simple_chr_driver: registered character device with major number = %i and minor numbers 0...255\n"
		, simple_chr_dev.device_major_no);
	return 0;
}

/**
* 设备注册
* 并自动创建设备节点并关联。和module_init绑定并被调用。利用了sysfs虚拟文件系统和udev动态设备管理器
* 1）sysfs挂载在/sys目录下，作用与proc有些类似，还有为linux统一设备模型作为管理之用。可以把设备和驱动程序的信息从内核输出到用户空间，以设备树的形式呈现。也可以用来对设备和驱动程序做设置，用户空间通过修改sysfs的文件属性来修改设备的属性值，进而改变设备的工作状态。
* 2）udev根据sysfs系统提供的设备信息实现对/dev目录下设备节点的动态管理，解决静态mknod命令创建大量节点麻烦、热插拔等问题。udev的规则配置文件是/etc/udev/udev.conf。类似的设备文件系统还有devfs，mdev（udev的简化版，常用在嵌入式系统中）等。
*/
static int register_device_auto_mknod(void)
{
	int result = 0;
	/* 步骤1：设备注册 */
	/*
	// 指定设备号的方式注册设备，不推荐
	// 设备注册：手动指定设备号和次设备号，返回dev_t类型的设备编号
	simple_chr_dev.device_no = MKDEV(simple_chr_dev.device_major_no, simple_chr_dev.device_minor_no);
	result = register_chrdev_region(simple_chr_dev.device_no, simple_chr_dev.number_of_devices, device_name);
	*/
	// 设备注册：由内核自动分配设备编号
	result = alloc_chrdev_region(&simple_chr_dev.device_no, 0, simple_chr_dev.number_of_devices, device_name);
	if (result < 0)
	{
		printk(KERN_WARNING "simple_chr_driver: alloc_chrdev_region fail with errorcode = %i\n", result);
		return result;
	}
	// 用宏MAJOR、MINOR分别解析出主设备号、次设备号
	printk(KERN_INFO "simple_chr_driver: alloc_chrdev_region successful major = %d, minor = %d\n", MAJOR(simple_chr_dev.device_no), MINOR(simple_chr_dev.device_no));

	/* 步骤2：创建cdev */
	// 动态申请cdev实体。不推荐静态声明的方式struct cdev cdev
	simple_chr_dev.simple_cdev = cdev_alloc();
	printk(KERN_INFO "simple_chr_driver: cdev_alloc successful\n");
	// cdev初始化
	cdev_init(simple_chr_dev.simple_cdev, &simple_driver_fops);
	// 将cdev加入到内核
	result = cdev_add(simple_chr_dev.simple_cdev, simple_chr_dev.device_no, simple_chr_dev.number_of_devices);
	if (result < 0)
	{
		printk(KERN_WARNING "simple_chr_driver: cdev_add fail with errorcode = %i\n", result);
		goto fail_cdev;
	}
	printk(KERN_INFO "simple_chr_driver: cdev_add successful\n");

	/* 步骤3：创建设备类 */
	// 创建设备类
	simple_chr_dev.simple_dev_class = class_create(THIS_MODULE, device_class_name);
	if (IS_ERR(simple_chr_dev.simple_dev_class))
	{
		printk(KERN_WARNING "simple_chr_driver: class_create fail\n");
		result = -EINVAL;
		goto fail_class;
	}

	/* 步骤4：创建节点 */
	// 使用设备类，创建指定名称的节点，位于/dev目录下
	simple_chr_dev.simple_dev = device_create(simple_chr_dev.simple_dev_class, NULL, simple_chr_dev.device_no, NULL, device_node_name);
	if (IS_ERR(simple_chr_dev.simple_dev))
	{
		printk(KERN_WARNING "simple_chr_driver: device_create fail\n");
		result = -EINVAL;
		goto fail_device;
	}

	/* 步骤5：获取设备树的属性内容 */
	simple_chr_dev.simple_dev_nd = of_find_node_by_path("/xxx");
	if (simple_chr_dev.simple_dev_nd == NULL)
	{
		result = -EINVAL;
		goto fail_findnd;
	}
	// 获取字符串
	result = of_property_read_string(simple_chr_dev.simple_dev_nd, "compatible", &str);
	if (result < 0) 
	{
		goto fail_rs;
	}
	else 
	{
		printk(KERN_INFO "simple_chr_driver: get device tree compatible is: %s\r\n", str);
	}

	/* 步骤6：硬件初始化。硬件资源的申请与配置，主要涉及地址映射 */
	// 使用ioremap函数，将I/O内存资源的物理地址映射到核心虚拟地址空间。不同开发板、设备映射的地址不同
	//volatile __u32 *gpio_led_reg = (__u32 *)ioremap(((__u32)0x3f200028), 4);
	printk(KERN_INFO "simple_chr_driver: registered character device successful\n");
	return 0;

// 使用goto跳转，分错误码处理失败并回滚
fail_rs:
fail_findnd:
	device_destroy(simple_chr_dev.simple_dev_class, simple_chr_dev.device_no);
fail_device:
	class_destroy(simple_chr_dev.simple_dev_class);
fail_class:
	cdev_del(&simple_chr_dev.simple_cdev);
fail_cdev:
	unregister_chrdev_region(simple_chr_dev.device_no, simple_chr_dev.number_of_devices);
fail_devid:
	return result;
}

void unregister_device(void)
{
	printk(KERN_INFO "simple_chr_driver: unregister_device() is called\n");
	if (simple_chr_dev.device_major_no != 0)
	{
		// 卸载字符设备
		unregister_chrdev(simple_chr_dev.device_major_no, device_name);
	}
}

void unregister_device_auto_delnod(void)
{
	// 摧毁设备
	device_destroy(simple_chr_dev.simple_dev_class, simple_chr_dev.device_no);
	// 摧毁类
	class_destroy(simple_chr_dev.simple_dev_class);
	// 删除字符设备
	cdev_del(simple_chr_dev.simple_cdev);
	// 释放字符设备号
	unregister_chrdev_region(simple_chr_dev.device_no, simple_chr_dev.number_of_devices);
	printk(KERN_INFO "simple_chr_driver: chrdev_exit successful\n");
}

// 定义文件操作集合，是驱动程序给上层应用程序访问硬件的接口。内核还支持其他操作指令
static struct file_operations simple_driver_fops =
{
	.owner = THIS_MODULE,
	// 指令和函数绑定，查看struct file_operations内核代码，不同的指令定义的函数签名不同
	// 当接收到打开指令时，调用device_open函数
	.open = device_open,
	// 当接收到读指令时，调用device_read函数
	.read = device_read,
	// 当接收到写指令时，调用device_write函数
	.write = device_write,
	// TODO 绑定ioctl操作实现命令控制功能
};

// 一个简单的读写示例，将数据从内核缓冲区复制到用户空间中分配的缓冲区
static int simple_var = 66;
// 和文件操作指令绑定时，函数的签名必须适合file_operations结构中的签名
static ssize_t device_read(struct file *filp, char *user_buf, size_t len, loff_t *off)
{
	printk(KERN_INFO "simple_chr_driver: device is read at offset = %i, read bytes len = %u\n", (int)*off, (unsigned int)len);
	// 将simple_var从内核空间复制到用户空间
	if (copy_to_user(user_buf, &simple_var, sizeof(int)))
	{ 
		return -EFAULT; 
	} 
	return sizeof(int);
}

static ssize_t device_write(struct file *filp, const char *user_buf, size_t len, loff_t *off) {
	printk(KERN_INFO "simple_chr_driver: device is write at offset = %i, write bytes len = %u\n", (int)*off, (unsigned int)len);
	// 将用户空间的数据复制到内核空间的simple_var 
	if (copy_from_user(&simple_var, user_buf, sizeof(int)))
	{ 
		return - EFAULT; 
	} 
	return sizeof(int); 
}

static ssize_t device_open(struct inode *inode, struct file *file) {
	printk(KERN_INFO "simple_chr_driver: device is open\n");
	return 0;
}

// 版权信息。linux内核是按照GPL发布的，同样linux的驱动程序也要提供版权信息，否则当加载到内核中系统会给出警告信息。
MODULE_LICENSE("GPL");
// 声明作者
MODULE_AUTHOR("handsomestWei");
// 模块描述
MODULE_DESCRIPTION("This is simple chr driver");
// 安装模块。在终端输入insmod指令时，会调用module_init，然后调用入参绑定的函数
// module_init(register_device);
module_init(register_device_auto_mknod);
// 卸载模块。在终端输入rmmod指令时，会调用module_exit，然后调用入参绑定的函数
// module_exit(unregister_device);
module_exit(unregister_device_auto_delnod);