TARGET_MODULE:=simple_chr_module
# ͨ���ں����������ֱ���ͼ��ط�ʽ
# ��ʽһ��ֱ�Ӱ��������������ں���
# KERNELRELEASE�����ں�Դ����ϲ�Makefile�ж����һ������
ifneq ($(KERNELRELEASE),)
    # kbuild��kernel build�����ڱ���Linux�ں��ļ�����Makefile���й����ϵ���չ��
	# kbuild�л�Ԥ����һЩ��������obj-y��obj-m������ָ��Ҫ���ɵ�.oĿ���ļ���ֻ��Ҫ�Ըñ������и�ֵ��kbuild�ͻ��Զ��Ѵ�����뵽�ں˻�����ģ�顣
    $(TARGET_MODULE)-objs := simple_chr_driver.o
    obj-m := $(TARGET_MODULE).o
# ��ʽ����������������Ϊģ�鵥�������.ko�ļ�������������ں��У�Ȼ���ֶ�����
else
    # ����ָ����ʹ��uname -r����ص�ǰʹ�õ��ں˰汾
    BUILDSYSTEM_DIR:=/lib/modules/$(shell uname -r)/build
    PWD:=$(shell pwd)
all : 
    # ����ģ��
    $(MAKE) -C $(BUILDSYSTEM_DIR) M=$(PWD) modules
clean:
    # ���������ļ���
    $(MAKE) -C $(BUILDSYSTEM_DIR) M=$(PWD) clean
load:
    # ��������
    insmod ./$(TARGET_MODULE).ko
unload:
	# ж������
    rmmod ./$(TARGET_MODULE).ko
endif