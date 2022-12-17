#pragma once
#include <linux/module.h>
#include <linux/init.h>
#include <linux/device.h>

// �ṹ��Ƕ����װ���߶�Ӧ���豸����������
struct simple_usb_device {
	int vendorId; // ���̱��
	struct device dev;
};

struct simple_usb_driver {
	int vendorId; // ���̱��
	struct device_driver drv;
};

// �豸����ֲ㣬�����ṩ�豸������ע��ж�غ�����������������Ϣ���豸�������㲻����ʽָ����������
int simple_usb_device_register(struct simple_usb_device *usb_dev);
void simple_usb_device_unregister(struct simple_usb_device *usb_dev);

int simple_usb_driver_register(struct simple_usb_driver *usb_drv);
void simple_usb_driver_unregister(struct simple_usb_driver *usb_drv);