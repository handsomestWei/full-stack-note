TARGET_MODULE:=simple_chr_module
# 通常内核驱动有两种编译和加载方式
# 方式一：直接把驱动程序编译进内核中
# KERNELRELEASE是在内核源码的上层Makefile中定义的一个变量
ifneq ($(KERNELRELEASE),)
    # kbuild即kernel build，用于编译Linux内核文件，对Makefile进行功能上的扩展。
	# kbuild中会预定义一些变量，如obj-y、obj-m，用来指定要生成的.o目标文件。只需要对该变量进行赋值，kbuild就会自动把代码编译到内核或编译成模块。
    $(TARGET_MODULE)-objs := simple_chr_driver.o
    obj-m := $(TARGET_MODULE).o
# 方式二：将驱动程序作为模块单独编译成.ko文件，而不编译进内核中，然后手动加载
else
    # 不用指定，使用uname -r命令返回当前使用的内核版本
    BUILDSYSTEM_DIR:=/lib/modules/$(shell uname -r)/build
    PWD:=$(shell pwd)
all : 
    # 构建模块
    $(MAKE) -C $(BUILDSYSTEM_DIR) M=$(PWD) modules
clean:
    # 清理构建文件夹
    $(MAKE) -C $(BUILDSYSTEM_DIR) M=$(PWD) clean
load:
    # 加载驱动
    insmod ./$(TARGET_MODULE).ko
unload:
	# 卸载驱动
    rmmod ./$(TARGET_MODULE).ko
endif