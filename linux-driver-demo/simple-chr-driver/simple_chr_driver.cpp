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


/* �Զ����豸�ṹ�� */
struct simple_chr_dev {
	// �豸��ţ���12λΪ���豸�ţ���20λΪ���豸��
	dev_t device_no = 0;
	// ���豸�ţ�����ĳһ����豸����Ҫ�Լ����壬���ܻ�������豸�ų�ͻ��������ϵͳ�Զ�����
	int device_major_no = 250;
	// ���豸�ţ���������ͬһ���豸�Ĳ�ͬ�����ͬ����
	int device_minor_no;
	// �豸����
	int number_of_devices = 1;
	// cdev��linux���������ַ��豸�Ľṹ�塣cdev������include/linux/cdev.h�У�ʵ����fs/char_dev.c��
	struct cdev *simple_cdev;
	// sysfsϵͳ���豸�ࡣ�Ƕ����豸�ĸ߼����󣬿������û��ռ�ĳ�������Լ�Ҫ����������������豸�������Ǹ����豸�����뵽ϵͳ�ķ�ʽ���豸�Ĺ���ԭ��������
	struct class *simple_dev_class;
	// �豸��
	struct device *simple_dev;
	// �豸�ڵ���
	struct device_node *simple_dev_nd;
};

struct simple_chr_dev simple_chr_dev;

// �豸����
const char device_name[] = "simple_chr_driver";
// �Զ���class�豸����������ʾ��/sys/classĿ¼����
const char device_class_name[] = "simple_chr_device_class";
// �豸�ڵ���
const char device_node_name[] = "simple_chr_device_node";

/**
* �豸ע��
* ��ɺ���Ҫ�ֶ������������ڵ㣬�������£�
* 1��ʹ��lsmod��cat /proc/modules����grep�����鿴ģ������豸��Ϊxxx
* 2�������豸�ڵ�mknod /dev/simple_chr_driver c xxx 0
* /proc/devices�е��豸�������������ɵģ���ʾ���ǵ�ǰ������ϵͳ��ģ�顣/dev�µ��豸��ͨ��mknod����ȥ�ģ��û�ͨ�����豸����������������Ӳ��ģ���һ�������װ��
*/
static int register_device(void)
{
	int result = 0;
	// printk�������������ں�̬�µĴ�ӡ��������printf�����������û�̬
	printk(KERN_INFO "simple_chr_driver: register_device() is called\n");
	// ע���ַ��豸���������豸�ţ���0��255֮�䡣��һ��������0��ʾ�豸����ϵͳ�Զ����ɡ�ע��ɹ����豸��������/proc/devices��
	result = register_chrdev(0, device_name, &simple_driver_fops);
	if (result < 0)
	{
		// ������־�ȼ������б���KERN_DEBUG���ȼ��ߵı���ӡ������̨�ϣ��ȼ��͵��������־�ļ���
		printk(KERN_WARNING "simple_chr_driver: can\'t register character device with errorcode = %i\n", result);
		return result;
	}
	simple_chr_dev.device_major_no = result;
	printk(KERN_INFO "simple_chr_driver: registered character device with major number = %i and minor numbers 0...255\n"
		, simple_chr_dev.device_major_no);
	return 0;
}

/**
* �豸ע��
* ���Զ������豸�ڵ㲢��������module_init�󶨲������á�������sysfs�����ļ�ϵͳ��udev��̬�豸������
* 1��sysfs������/sysĿ¼�£�������proc��Щ���ƣ�����Ϊlinuxͳһ�豸ģ����Ϊ����֮�á����԰��豸�������������Ϣ���ں�������û��ռ䣬���豸������ʽ���֡�Ҳ�����������豸���������������ã��û��ռ�ͨ���޸�sysfs���ļ��������޸��豸������ֵ�������ı��豸�Ĺ���״̬��
* 2��udev����sysfsϵͳ�ṩ���豸��Ϣʵ�ֶ�/devĿ¼���豸�ڵ�Ķ�̬���������̬mknod����������ڵ��鷳���Ȳ�ε����⡣udev�Ĺ��������ļ���/etc/udev/udev.conf�����Ƶ��豸�ļ�ϵͳ����devfs��mdev��udev�ļ򻯰棬������Ƕ��ʽϵͳ�У��ȡ�
*/
static int register_device_auto_mknod(void)
{
	int result = 0;
	/* ����1���豸ע�� */
	/*
	// ָ���豸�ŵķ�ʽע���豸�����Ƽ�
	// �豸ע�᣺�ֶ�ָ���豸�źʹ��豸�ţ�����dev_t���͵��豸���
	simple_chr_dev.device_no = MKDEV(simple_chr_dev.device_major_no, simple_chr_dev.device_minor_no);
	result = register_chrdev_region(simple_chr_dev.device_no, simple_chr_dev.number_of_devices, device_name);
	*/
	// �豸ע�᣺���ں��Զ������豸���
	result = alloc_chrdev_region(&simple_chr_dev.device_no, 0, simple_chr_dev.number_of_devices, device_name);
	if (result < 0)
	{
		printk(KERN_WARNING "simple_chr_driver: alloc_chrdev_region fail with errorcode = %i\n", result);
		return result;
	}
	// �ú�MAJOR��MINOR�ֱ���������豸�š����豸��
	printk(KERN_INFO "simple_chr_driver: alloc_chrdev_region successful major = %d, minor = %d\n", MAJOR(simple_chr_dev.device_no), MINOR(simple_chr_dev.device_no));

	/* ����2������cdev */
	// ��̬����cdevʵ�塣���Ƽ���̬�����ķ�ʽstruct cdev cdev
	simple_chr_dev.simple_cdev = cdev_alloc();
	printk(KERN_INFO "simple_chr_driver: cdev_alloc successful\n");
	// cdev��ʼ��
	cdev_init(simple_chr_dev.simple_cdev, &simple_driver_fops);
	// ��cdev���뵽�ں�
	result = cdev_add(simple_chr_dev.simple_cdev, simple_chr_dev.device_no, simple_chr_dev.number_of_devices);
	if (result < 0)
	{
		printk(KERN_WARNING "simple_chr_driver: cdev_add fail with errorcode = %i\n", result);
		goto fail_cdev;
	}
	printk(KERN_INFO "simple_chr_driver: cdev_add successful\n");

	/* ����3�������豸�� */
	// �����豸��
	simple_chr_dev.simple_dev_class = class_create(THIS_MODULE, device_class_name);
	if (IS_ERR(simple_chr_dev.simple_dev_class))
	{
		printk(KERN_WARNING "simple_chr_driver: class_create fail\n");
		result = -EINVAL;
		goto fail_class;
	}

	/* ����4�������ڵ� */
	// ʹ���豸�࣬����ָ�����ƵĽڵ㣬λ��/devĿ¼��
	simple_chr_dev.simple_dev = device_create(simple_chr_dev.simple_dev_class, NULL, simple_chr_dev.device_no, NULL, device_node_name);
	if (IS_ERR(simple_chr_dev.simple_dev))
	{
		printk(KERN_WARNING "simple_chr_driver: device_create fail\n");
		result = -EINVAL;
		goto fail_device;
	}

	/* ����5����ȡ�豸������������ */
	simple_chr_dev.simple_dev_nd = of_find_node_by_path("/xxx");
	if (simple_chr_dev.simple_dev_nd == NULL)
	{
		result = -EINVAL;
		goto fail_findnd;
	}
	// ��ȡ�ַ���
	result = of_property_read_string(simple_chr_dev.simple_dev_nd, "compatible", &str);
	if (result < 0) 
	{
		goto fail_rs;
	}
	else 
	{
		printk(KERN_INFO "simple_chr_driver: get device tree compatible is: %s\r\n", str);
	}

	/* ����6��Ӳ����ʼ����Ӳ����Դ�����������ã���Ҫ�漰��ַӳ�� */
	// ʹ��ioremap��������I/O�ڴ���Դ�������ַӳ�䵽���������ַ�ռ䡣��ͬ�����塢�豸ӳ��ĵ�ַ��ͬ
	//volatile __u32 *gpio_led_reg = (__u32 *)ioremap(((__u32)0x3f200028), 4);
	printk(KERN_INFO "simple_chr_driver: registered character device successful\n");
	return 0;

// ʹ��goto��ת���ִ����봦��ʧ�ܲ��ع�
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
		// ж���ַ��豸
		unregister_chrdev(simple_chr_dev.device_major_no, device_name);
	}
}

void unregister_device_auto_delnod(void)
{
	// �ݻ��豸
	device_destroy(simple_chr_dev.simple_dev_class, simple_chr_dev.device_no);
	// �ݻ���
	class_destroy(simple_chr_dev.simple_dev_class);
	// ɾ���ַ��豸
	cdev_del(simple_chr_dev.simple_cdev);
	// �ͷ��ַ��豸��
	unregister_chrdev_region(simple_chr_dev.device_no, simple_chr_dev.number_of_devices);
	printk(KERN_INFO "simple_chr_driver: chrdev_exit successful\n");
}

// �����ļ��������ϣ�������������ϲ�Ӧ�ó������Ӳ���Ľӿڡ��ں˻�֧����������ָ��
static struct file_operations simple_driver_fops =
{
	.owner = THIS_MODULE,
	// ָ��ͺ����󶨣��鿴struct file_operations�ں˴��룬��ͬ��ָ���ĺ���ǩ����ͬ
	// �����յ���ָ��ʱ������device_open����
	.open = device_open,
	// �����յ���ָ��ʱ������device_read����
	.read = device_read,
	// �����յ�дָ��ʱ������device_write����
	.write = device_write,
	// TODO ��ioctl����ʵ��������ƹ���
};

// һ���򵥵Ķ�дʾ���������ݴ��ں˻��������Ƶ��û��ռ��з���Ļ�����
static int simple_var = 66;
// ���ļ�����ָ���ʱ��������ǩ�������ʺ�file_operations�ṹ�е�ǩ��
static ssize_t device_read(struct file *filp, char *user_buf, size_t len, loff_t *off)
{
	printk(KERN_INFO "simple_chr_driver: device is read at offset = %i, read bytes len = %u\n", (int)*off, (unsigned int)len);
	// ��simple_var���ں˿ռ临�Ƶ��û��ռ�
	if (copy_to_user(user_buf, &simple_var, sizeof(int)))
	{ 
		return -EFAULT; 
	} 
	return sizeof(int);
}

static ssize_t device_write(struct file *filp, const char *user_buf, size_t len, loff_t *off) {
	printk(KERN_INFO "simple_chr_driver: device is write at offset = %i, write bytes len = %u\n", (int)*off, (unsigned int)len);
	// ���û��ռ�����ݸ��Ƶ��ں˿ռ��simple_var 
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

// ��Ȩ��Ϣ��linux�ں��ǰ���GPL�����ģ�ͬ��linux����������ҲҪ�ṩ��Ȩ��Ϣ�����򵱼��ص��ں���ϵͳ�����������Ϣ��
MODULE_LICENSE("GPL");
// ��������
MODULE_AUTHOR("handsomestWei");
// ģ������
MODULE_DESCRIPTION("This is simple chr driver");
// ��װģ�顣���ն�����insmodָ��ʱ�������module_init��Ȼ�������ΰ󶨵ĺ���
// module_init(register_device);
module_init(register_device_auto_mknod);
// ж��ģ�顣���ն�����rmmodָ��ʱ�������module_exit��Ȼ�������ΰ󶨵ĺ���
// module_exit(unregister_device);
module_exit(unregister_device_auto_delnod);