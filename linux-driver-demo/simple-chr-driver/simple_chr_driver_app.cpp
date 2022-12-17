#include <fcntl.h>
#include <stdio.h>
#include <io.h>
#include <process.h>

/**
 * �����������ϲ�Ӧ�ô���
 * 1�����룺gcc simple_chr_driver_app.c -o simple_chr_driver_app
 * 2�����ã�./simple_chr_driver_app
 */
int main(void)
{
	int fd;
	int num = 88;
	// �Զ�д��ʽ���豸�ļ�������������ж�Ӧ���ļ�����ָ��󶨵ĺ���
	fd = open("/dev/simple_chr_driver", O_RDWR);
	if (fd < 0) {
		printf("can't open /dev/simple_chr_driver");
		return 0;
	}

	// ���ζ�
	read(fd, &num, sizeof(int));
	printf("The simple var is %d\n", num);

	// д
	printf("Please input the num written to simple var\n");
	scanf("%d", &num);
	write(fd, &num, sizeof(int));

	// �ٴζ�
	read(fd, &num, sizeof(int));
	printf("The simple var is %d\n", num);

	// �ر�
	close(fd);
	return 0;
}