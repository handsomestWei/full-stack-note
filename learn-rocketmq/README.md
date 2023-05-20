# learn-rocketmq
**spring cloud stream**+**spring integration**整合**rocketmq**   
+ 事务消息
+ 消息拉取

## rocketmq架构
图源官网
<div align=center><img width="1216" height="571" src="https://github.com/handsomestWei/full-stack-note/blob/main/learn-rocketmq/docs/rocketmq_architecture_3.png" /></div>

## rocketmq windows环境运行
[rocketmq安装](https://rocketmq.apache.org/docs/quick-start/)

### 环境变量
```
变量名：ROCKETMQ_HOME
变量值：mq目录
```
### name server
NameServer是一个非常简单的Topic路由注册中心，支持Broker的动态注册与发现。主要包括两个功能：Broker管理和路由信息管理。
```
cd mq目录\bin\
start mqnamesrv.cmd
```
### broker server
Broker主要负责消息的存储、投递和查询以及服务高可用保证。
```
cd mq目录\bin\
start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true -c /xxx/conf/broker.conf
其中-n指定nameserver地址
其中-c指定参数
```
```
若提示xxx文件不存在，尝试修改runbroker.cmd
修改前 set "JAVA_OPT=%JAVA_OPT% -cp %CLASSPATH%"
修改后 set "JAVA_OPT=%JAVA_OPT% -cp "%CLASSPATH%""
```
#### brocker.conf配置
<details>
<summary>配置例</summary>
<pre><code>#删除文件时间点，默认是凌晨4点
deleteWhen=04
#文件保留时间，默认48小时
fileReservedTime=120
#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824
#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000
#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88
#存储路径
storePathRootDir=D:\\rocketmq\\store
#commitLog存储路径
storePathCommitLog=D:\\rocketmq\\store\\commitlog
#消费队列存储路径
storePathConsumeQueue=D:\\rocketmq\\store\\consumequeue
#消息索引存储路径
storePathIndex=D:\\rocketmq\\store\\index
#checkpoint 文件存储路径
storeCheckpoint=D:\\rocketmq\\store\\checkpoint
#abort 文件存储路径
abortFile=D:\\rocketmq\\store\\abort
#限制的消息大小
maxMessageSize=65536
# Broker 的角色
# - ASYNC_MASTER 异步复制Master
# - SYNC_MASTER 同步双写Master
# - SLAVE
brokerRole=ASYNC_MASTER
# 刷盘方式
# - ASYNC_FLUSH 异步刷盘
# - SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH
</code></pre>
</details>

### rocketmq控制台
[rocketmq扩展下载](https://github.com/apache/rocketmq-externals.git)
#### 控制台配置
```
cd rocketmq-externals\rocketmq-console\src\main\resources

修改application.properties配置文件
# 配置控制台地址和端口
server.address=127.0.0.1
server.port=8080
# 配置nameserver地址
rocketmq.config.namesrvAddr=127.0.0.1:9876
```
#### 控制台编译启动
```
cd \rocketmq-externals\rocketmq-console
mvn clean package -Dmaven.test.skip=true
cd \rocketmq-externals\rocketmq-console\target
java -jar rocketmq-console-ng-x.x.x.jar
```

#### 控制台访问
```
http://127.0.0.1:8080
```

### 日志
```
默认目录在C:\Users\xxx\logs\rocketmqlogs
可修改xxx/conf/logback_xxx.xml文件指定日志存储目录
```

### 一点坑
+ mq运行内存占用较大，可以修改启动脚本调整jvm分配
+ mq的store存储目录占用空间较大，默认在C:\Users\xxx\store，空间不够触发阈值，会导致消息无法写入。可以修改broker.conf指定存储目录和commitlog文件大小

## 参考
+ [rocketmq中文文档](https://github.com/apache/rocketmq/tree/master/docs/cn)
+ [rocketmq connect中文指南](https://github.com/apache/rocketmq-externals/blob/master/docs/connect/cn/README.md)
+ [dledger 基于raft协议的commitlog存储库](https://github.com/openmessaging/openmessaging-storage-dledger)
+ [rocketmq官方demo](https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples/rocketmq-example)
+ [阿里云大学RocketMQ Binder](https://edu.aliyun.com/course/1942)
