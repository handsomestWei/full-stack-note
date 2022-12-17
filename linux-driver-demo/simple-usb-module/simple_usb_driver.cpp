#include "simple_usb_bus.h"

/* �豸����ģ��֮���� */

struct simple_usb_driver simple_usb_driver = {
	.vendorId = 0x1122,
	.drv = {
		.name = device_name,
		.probe = usb_driver_probe, // ��probe�����������߻ص�������ƥ�䵽�豸�󣬵���������probe�ӿ��������豸
		.remove = usb_driver_remove,
	},
};

int usb_driver_probe(struct device *dev)
{
	printk(KERN_INFO "simple_usb_driver: driver probe\n");
	// ������������ڣ�����ϸ��ʵ��
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
	// �豸����ģ�͵�����ע�ᣬע��ɹ�����/sys/bus/usb/driverĿ¼�´���Ŀ¼simple_usb_driver
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

// ��װģ�顣���ն�����insmodָ��ʱ�������module_init��Ȼ�������ΰ󶨵ĺ���
module_init(usb_driver_init);
// ж��ģ�顣���ն�����rmmodָ��ʱ�������module_exit��Ȼ�������ΰ󶨵ĺ���
module_exit(usb_driver_exit);
// ��Ȩ��Ϣ��linux�ں��ǰ���GPL�����ģ�ͬ��linux����������ҲҪ�ṩ��Ȩ��Ϣ�����򵱼��ص��ں���ϵͳ�����������Ϣ��
MODULE_LICENSE("GPL");
MODULE_AUTHOR("handsomestWei");
