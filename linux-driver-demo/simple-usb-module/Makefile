obj-m += simple_usb_device.o simple_usb_driver.o simple_usb_bus.o

KDIR := /xxx/linux-xxx
all:
	make -C $(KDIR) M=`pwd` modules
clean:
	make -C $(KDIR) M=`pwd` modules clean
	rm -f modules.order