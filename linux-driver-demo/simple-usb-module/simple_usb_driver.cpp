#include "simple_usb_bus.h"

/* 设备驱动模型之驱动 */

struct simple_usb_driver simple_usb_driver = {
	.vendorId = 0x1122,
	.drv = {
		.name = device_name,
		.probe = usb_driver_probe, // 绑定probe函数，给总线回调。驱动匹配到设备后，调用驱动的probe接口来驱动设备
		.remove = usb_driver_remove,
	},
};

int usb_driver_probe(struct device *dev)
{
	printk(KERN_INFO "simple_usb_driver: driver probe\n");
	// 驱动的真正入口，驱动细节实现
	return 0;
}

int usb_driver_remove(struct device *dev)
{
	printk(KERN_INFO "simple_usb_driver: driver remove\n");
	return 0;
}

static int usb_driver_init(void)
{
	int ret;
	// 设备驱动模型的驱动注册，注册成功后在/sys/bus/usb/driver目录下创建目录simple_usb_driver
	ret = simple_usb_driver_register(&simple_usb_driver);
	if (ret) {
		printk(KERN_WARNING "simple_usb_driver: driver register failed!\n");
		return ret;
	}
	printk(KERN_INFO "simple_usb_driver: driver init\n");
	return 0;
}

static void usb_driver_exit(void)
{
	simple_usb_device_unregister(&simple_usb_driver);
	printk(KERN_INFO "simple_usb_driver: driver exit\n");
}

// 安装模块。在终端输入insmod指令时，会调用module_init，然后调用入参绑定的函数
module_init(usb_driver_init);
// 卸载模块。在终端输入rmmod指令时，会调用module_exit，然后调用入参绑定的函数
module_exit(usb_driver_exit);
// 版权信息。linux内核是按照GPL发布的，同样linux的驱动程序也要提供版权信息，否则当加载到内核中系统会给出警告信息。
MODULE_LICENSE("GPL");
MODULE_AUTHOR("handsomestWei");
