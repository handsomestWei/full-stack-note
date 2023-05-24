# learn-kafka
kafka入门。整合springboot

## 包括
+ kafka生产者：使用`KafkaTemplate`发送消息
+ kafka消费者：使用`@KafkaListener`接收消息
+ kafka事务：配置`spring.kafka.producer.transaction-id-prefix`事务名称前缀开启事务，配置`spring.kafka.consumer.isolation-level`定义事务隔离级别
+ kafka主题间数据传输：使用`@SendTo`实现简单的延迟队列

## kafka docker单机环境搭建
### 拉取镜像
包括zk、kafka、kafka集群管理器
```
docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka
docker pull sheepkiller/kafka-manager
```
### 运行
```
docker run -d --name zookeeper --publish 2181:2181 \--volume /etc/localtime:/etc/localtime \--restart=always \wurstmeister/zookeeper
docker run -d --name kafka --publish 9092:9092 \--link zookeeper:zookeeper \--env KAFKA_BROKER_ID=100 \--env HOST_IP=192.168.1.108 \--env KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \--env KAFKA_ADVERTISED_HOST_NAME=192.168.1.108 \--env KAFKA_ADVERTISED_PORT=9092 \--restart=always \--volume /etc/localtime:/etc/localtime \wurstmeister/kafka
docker run -d --name kafka-manager \--link zookeeper:zookeeper \--link kafka:kafka -p 9001:9000 \--restart=always \--env ZK_HOSTS=zookeeper:2181 \sheepkiller/kafka-manager
```
### 管理
```
使用ZooInspector工具查看zk的kafka目录
kafka控制台访问http://192.168.1.108:9001/
```