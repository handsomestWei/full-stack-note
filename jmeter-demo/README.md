# jmeter-demo
使用jmeter进行压力测试

# Usage
## 依赖jdk安装
```
yum install java-1.8.0-openjdk
```
版本验证
```
java -version
```
## jmeter安装
[官网安装包下载](https://jmeter.apache.org/download_jmeter.cgi)
选择二进制文件，解压
```
tar -xzvf apache-jmeter-x.tgz
```
环境变量配置
```
vi /etc/profile
```
添加以下内容
```
export JMETER=/opt/apache-jmeter-x
export CLASSPATH=$JMETER/lib/ext/ApacheJMeter_core.jar:$JMETER/lib/jorphan.jar:$JMETER/lib/logkit-2.0.jar:$CLASSPATH
export PATH=$JMETER/bin/:$PATH
```
使配置生效
```
source /etc/profile
```
版本验证
```
jmeter -v
```

## 单机运行
```
jmeter -n -t /root/wjy/TestPlan.jmx -l /root/wjy/TestResult.jtl -e -o /root/wjy/jmeterReport
```

## 集群运行
### 配置多台控制机
修改jmeter运行目录下/bin/jmeter.properties
```
remote_hosts=Master,ip2,ip3
```
### 配置远程密钥
```
运行/bin/create-rmi-keystore.sh
将生成的rmi_keystore.jks拷贝到各台机器jmeter目录/bin下
```
### 集群运行
执行所有的节点
```
jmeter -n -t /root/wjy/TestPlan.jmx -r -l /root/wjy/TestResult.jtl -e -o /root/wjy/jmeterReport
```
执行指定节点ip1
```
jmeter -n -t /root/wjy/TestPlan.jmx -R ip1 -l /root/wjy/TestResult.jtl -e -o /root/wjy/jmeterReport
```

## jmeter报告指标说明
### Statistics统计
```
Samples：测试过程中发出的请求数
KO：失败数量
Error：失败率
Average：平均响应时间，单位毫秒
Min：最小响应时间，单位毫秒
Max：最大响应时间，单位毫秒
90th/95th/99thpct：请求响应时间按照从小到大排序后第90%/95%/99%的线程响应时间，代表90%/95%/99%的请求的响应时间在这个范围之内
Throughput：吞吐量，每毫秒完成的请求数量
```
### TPS每秒事务数
```
页面在Chart->Throughput->Transactions Per Second   
是用来衡量系统处理能力的一个重要指标
```

## 关于ramp-up period配置
