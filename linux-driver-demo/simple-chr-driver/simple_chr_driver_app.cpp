#include <fcntl.h>
#include <stdio.h>
#include <io.h>
#include <process.h>

/**
 * 测试驱动的上层应用代码
 * 1）编译：gcc simple_chr_driver_app.c -o simple_chr_driver_app
 * 2）调用：./simple_chr_driver_app
 */
int main(void)
{
	int fd;
	int num = 88;
	// 以读写方式打开设备文件，会调用驱动中对应的文件操作指令绑定的函数
	fd = open("/dev/simple_chr_driver", O_RDWR);
	if (fd < 0) {
		printf("can't open /dev/simple_chr_driver");
		return 0;
	}

	// 初次读
	read(fd, &num, sizeof(int));
	printf("The simple var is %d\n", num);

	// 写
	printf("Please input the num written to simple var\n");
	scanf("%d", &num);
	write(fd, &num, sizeof(int));

	// 再次读
	read(fd, &num, sizeof(int));
	printf("The simple var is %d\n", num);

	// 关闭
	close(fd);
	return 0;
}