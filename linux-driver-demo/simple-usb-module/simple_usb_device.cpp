#include "simple_usb_bus.h"

/* �豸����ģ��֮�豸 */

// �Զ����豸����
struct simple_usb_device simple_usb_device = {
	.vendorId = 0x1122,
	.dev = {
		.bus_id = "simple_usb_device",
		.release = usb_dev_release,	// ���豸ж�غ�������Ȼж��ʱ�����
	},
};

// �豸ж�غ���
void usb_dev_release(struct device *dev)
{
	printk(KERN_INFO "simple_usb_device: device release\n");
}

static int usb_device_init(void)
{
	int ret;
	ret = simple_usb_device_register(&simple_usb_device);
	if (ret) {
		printk(KERN_WARNING "simple_usb_device: device register failed\n");
		return ret;
	}
	printk(KERN_INFO "simple_usb_device: device init\n");
	return 0;
}

static void usb_device_exit(void)
{
	simple_usb_device_unregister(&simple_usb_device);
	printk(KERN_INFO "simple_usb_device: device exit\n");
}

module_init(usb_device_init);
module_exit(usb_device_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("handsomestWei");
