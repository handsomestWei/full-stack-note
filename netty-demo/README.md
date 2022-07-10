# netty-demo
使用**netty**实现简单的回显

# Usage
服务端监听指定端口，使用telnet连接并打印输入
```
telnet [ip] [port] 
```

## 操作系统epoll调用
### jdk nio实现过程
```
1）网络nio入口java.nio.channels.ServerSocketChannel#open
2）按不同操作系统区分jdk/src/solaris/classes/sun/nio/ch/DefaultSelectorProvider.java
3）linux则调用jdk/src/solaris/classes/sun/nio/ch/EPollSelectorProvider.java
4）实现类jdk/src/solaris/classes/sun/nio/ch/EPollSelectorProvider.java#doSelect
5）最终调用jdk/src/solaris/classes/sun/nio/ch/EPollArrayWrapper.java#poll
6）本地实现jdk/src/solaris/native/sun/nio/ch/EPollArrayWrapper.c
```

### netty实现过程
```
1) 构造函数io.netty.channel.epoll.EpollEventLoop#EpollEventLoop
2）本地方法工具类io.netty.channel.epoll.Native
```