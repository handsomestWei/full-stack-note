# canal-demo
alibaba canal demo

## canal服务端
[canal-deployer](https://github.com/alibaba/canal/wiki/QuickStart)

### 服务端配置
启动脚本
```
windows环境修改：\bin\startup.bat
去掉@rem注释，放开logback_configurationFile配置
```
单实例
```
修改数据库连接配置：\conf\example\instance.properties

canal.instance.master.address=172.16.18.6:3306
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal
```

## canal控制台
[canal-admin](https://github.com/alibaba/canal/wiki/Canal-Admin-Guide)

## canal客户端
[canal-example](https://github.com/alibaba/canal/wiki/ClientExample)
```
// 监听指定数据库实例canal下的所有表
connector.subscribe("canal\\..*"); 
```
## 和otter区别
[otter](https://github.com/alibaba/otter)目前嵌入式依赖canal，作为canal的消费端