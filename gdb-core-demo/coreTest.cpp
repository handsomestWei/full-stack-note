#include <iostream>
#include <stdio.h>

using namespace std;
void coreTest()
{
    int i = 0;
	// 发生崩溃，i前面应该加上&取地址符
    scanf("%d", i);
    printf(%d, i);
}


int main()
{
    coreTest();
    return 0;
}