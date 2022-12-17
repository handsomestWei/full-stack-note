#include "simple_usb_bus.h"

/* 设备驱动模型之总线 */

// 自定义总线类型
struct bus_type simple_usb_bus = {
	.name = "simple_usb_bus",	// 注册成功后将在/sys/bus目录下看到目录simple_usb_bus
	.match = usb_bus_match, // 绑定匹配函数，让总线和设备、驱动匹配
};

// 匹配函数，判断设备、驱动是否和总线匹配
int usb_bus_match(struct device *dev, struct device_driver *drv)
{	
	struct simple_usb_device *usb_dev = container_of(dev, struct simple_usb_device, dev);
	struct simple_usb_driver *usb_drv = container_of(drv, struct simple_usb_driver, drv);
	// 检查厂商id是否匹配
	if (usb_dev->vendorId == usb_drv->vendorId)
	{
		printk(KERN_INFO "simple_usb_bus: match success\n");
		return 1;
	}
	else {
		printk(KERN_INFO "simple_usb_bus: match failed\n");
		return 0;
	}
}

static int usb_bus_init(void)
{
	int ret;
	ret = bus_register(&simple_usb_bus);
	if (ret) {
		printk(KERN_WARNING "simple_usb_bus: bus register failed\n");
		return ret;
	}
	printk(KERN_INFO "simple_usb_bus: bus init\n");
	return 0;
}

static void usb_bus_exit(void)
{
	bus_unregister(&simple_usb_bus);
	printk(KERN_INFO "simple_usb_bus: bus exit\n");
}

int simple_usb_device_register(struct simple_usb_device *usb_dev)
{
	usb_dev->dev.bus = &simple_usb_bus;
	// 和平台设备platform_device_register类似，platform_device是device的派生
	return device_register(&usb_dev->dev);
}

void simple_usb_device_unregister(struct simple_usb_device *usb_dev)
{
	return device_unregister(&usb_dev->dev);
}

int simple_usb_driver_register(struct simple_usb_driver *usb_drv)
{
	usb_drv->drv.bus = &simple_usb_bus;
	// 驱动注册，注册成功后在/sys/bus/usb/driver目录下创建目录simple_usb_driver
	return driver_register(&usb_drv->drv);
}

void simple_usb_driver_unregister(struct simple_usb_driver *usb_drv)
{
	return device_unregister(&usb_drv->drv);
}

// 导出符号simple_usb_bus，让设备和驱动模块可以引用
EXPORT_SYMBOL(simple_usb_bus);

module_init(usb_bus_init);
module_exit(usb_bus_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("handsomestWei");
