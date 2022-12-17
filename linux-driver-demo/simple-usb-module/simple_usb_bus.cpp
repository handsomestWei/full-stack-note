#include "simple_usb_bus.h"

/* �豸����ģ��֮���� */

// �Զ�����������
struct bus_type simple_usb_bus = {
	.name = "simple_usb_bus",	// ע��ɹ�����/sys/busĿ¼�¿���Ŀ¼simple_usb_bus
	.match = usb_bus_match, // ��ƥ�亯���������ߺ��豸������ƥ��
};

// ƥ�亯�����ж��豸�������Ƿ������ƥ��
int usb_bus_match(struct device *dev, struct device_driver *drv)
{	
	struct simple_usb_device *usb_dev = container_of(dev, struct simple_usb_device, dev);
	struct simple_usb_driver *usb_drv = container_of(drv, struct simple_usb_driver, drv);
	// ��鳧��id�Ƿ�ƥ��
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
	// ��ƽ̨�豸platform_device_register���ƣ�platform_device��device������
	return device_register(&usb_dev->dev);
}

void simple_usb_device_unregister(struct simple_usb_device *usb_dev)
{
	return device_unregister(&usb_dev->dev);
}

int simple_usb_driver_register(struct simple_usb_driver *usb_drv)
{
	usb_drv->drv.bus = &simple_usb_bus;
	// ����ע�ᣬע��ɹ�����/sys/bus/usb/driverĿ¼�´���Ŀ¼simple_usb_driver
	return driver_register(&usb_drv->drv);
}

void simple_usb_driver_unregister(struct simple_usb_driver *usb_drv)
{
	return device_unregister(&usb_drv->drv);
}

// ��������simple_usb_bus�����豸������ģ���������
EXPORT_SYMBOL(simple_usb_bus);

module_init(usb_bus_init);
module_exit(usb_bus_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("handsomestWei");
