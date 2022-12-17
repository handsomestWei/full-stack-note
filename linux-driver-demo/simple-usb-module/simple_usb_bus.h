#pragma once
#include <linux/module.h>
#include <linux/init.h>
#include <linux/device.h>

// 结构内嵌，封装总线对应的设备和驱动对象
struct simple_usb_device {
	int vendorId; // 厂商编号
	struct device dev;
};

struct simple_usb_driver {
	int vendorId; // 厂商编号
	struct device_driver drv;
};

// 设备管理分层，总线提供设备和驱动注册卸载函数。屏蔽了总线信息，设备和驱动层不用显式指定总线类型
int simple_usb_device_register(struct simple_usb_device *usb_dev);
void simple_usb_device_unregister(struct simple_usb_device *usb_dev);

int simple_usb_driver_register(struct simple_usb_driver *usb_drv);
void simple_usb_driver_unregister(struct simple_usb_driver *usb_drv);