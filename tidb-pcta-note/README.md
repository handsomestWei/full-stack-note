# tidb-pcta-note
PingCAP TiDB数据库专员PCTA认证学习笔记

## 体系架构
### 数据库设计
```
存算分离。三层架构：PD（Placement Driver）负责集群元信息管理和调度，TiDB负责sql计算，TiKV负责存储
```

### 存储引擎
```
1、基于LSM-Tree的RocksDB引擎，比B-Tree写入更快，用空间置换写入延迟
2、数据冗余副本：multi raft-group副本机制。基于raft，比Paxos更简单
3、弹性扩展：数据动态分片而不是预先分片，使用基于range范围的分片算法
```

### sql计算引擎
```
1、分布式事务模型：MVCC，去中心化两阶段提交。事务默认隔离级别是SI（快照隔离），也支持RC(read commit)
2、计算：协作处理器Coprocessor
3、在kv上实现逻辑表：key=tableid+rowid+indexid，value=所有列按等位偏移的方式进行connect进行连接
```

### HATP
```
1、针对OLAP引入Ti-spark，避免OOM，缓解算力问题
2、列式引擎上引入了delta tree，实现准实时更新的列式引擎Ti-flash用于OLAP数据副本
3、副本同步到列式引擎：Ti-Flash以Raft Learner的方式接入Multi-Raft组，使用异步方式传输数据，将行格式拆解为列格式
4、MPP解决计算节点的扩展与并行计算：OLAP的场景引入MPP并行计算框架
```

## 集群管理
### 集群部署
```
1、TiUP工具集群运维工具
2、安装顺序TiUP->TiUP cluster->配置集群拓扑文件->check检查配置参数->deploy部署->start启动->display查看运行状态
3、集群启动顺序PD->TiKV->TiDB->TiFlash，停止顺序反向
```

### 对mysql支持
```
1、支持类mysql 5.7协议连接客户端
2、不支持mysql存储过程、触发器、外键、函数等
```

### TiDB参数
```
参数配置
1、系统参数配置：通过客户端连接修改，只针对tidb-server参数，配置参数持久化到TiKV
2、集群参数配置：通过tiup cluster edit-config命令修改，针对PD、TiKV、tidb-server等配置参数，保存在各自节点的配置文件中。配置后重启节点才生效。
```
```
系统参数作用域
1、global全局级别：不影响当前回话，影响新回话
2、session回话级别：立即生效，只影响当前回话，回话断开后失效
```

### 扩缩容
```
1、扩容scale-out，缩容scale-in
2、TiDB、TiKV、PD方式一样
3、TiFlash需要先根据剩余节点数调整数据库表的副本数
```

### 集群升级
```
1、只针对cluster，不是kv、db等
2、补丁升级：patch
3、大版本升级：不支持跨版本。升级TiUP->修改集群拓扑文件配置参数使得适应新版本->集群健康状态检查->升级->验证
4、升级方式：不支持回退。upgrade默认不停机，也可停机离线升级--offline
5、升级报错中断：audit查看失败日志，replay继续上次升级
```

## 用户管理
```
1、用户和角色都存在mysql.users表
2、角色没有账号密码，能包含其他角色，能像用户一样授权
3、角色是被锁住的，用户登录后要用命令开启被赋予的角色
```

## 文件与日志管理
```
1、每个组件都有日志文件、配置文件
2、tidb-server没有数据文件
3、tiup cluster edit-config命令查看配置
```

## 监控
```
1、额外安装：Grafana展示+Prometheus采集+Alertmanager告警，关注集群状态信息
2、4.0后Dashboard监控体系，没有告警功能，安装PD节点后自带无需独立部署，主要关注sql性能检测和诊断等。
```

## 数据备份
### 备份策略
```
1、热备：可读写，不停机。备份时影响性能。
2、温备：可读不可写，不停机。备份时影响性能。
3、冷备：不可读写，停机拷贝文件。也是物理备份。要容忍宕机，成本低。
```
```
1、逻辑备份：导出sql或csv文件，可在异构数据库或系统迁移。适合小数据，速度慢。
2、物理备份：复制二进制数据文件，只能迁移到相同存储引擎，可以跨机器架构。适合大数据，速度快。
3、基于复制的备份：binlog日志主从同步的异步复制，需要增加从库成本，但消除对注主库的性能影响。
```

### TiDB BR工具
```
1、数据备份和恢复，只针对TiDB
2、Backup可全量、增量导出
3、Restore可全量、增量导入
4、数据量较大，热备，物理备份
5、直接拷贝TiKV 键值对SST文件
6、支持数据一致性检查
7、粒度为db实例、某表
8、由TiKV里的各Region Leader负责
```

## 数据迁移
### TiDB Dumpling工具
```
1、只能全量导出
2、数据量较小，热备，逻辑备份
3、连接tidb-server
4、导出sql或csv文件，可在异构数据库或系统迁移
5、粒度到某行数据，可根据where条件过滤
```

### TiDB Lightning工具
```
1、全量导入，只针对TiDB
2、数据源是Dumpling的导出，连接tidb-server
3、后端backend：Local-backend不用转储高版本4.0后适用，Importer-backend要转储和额外组件，TiDB-backend所有版本都可以且目标表可以不为空生产环境建议使用
4、主要功能：断点续传，库表过滤，并行导入，web界面管理任务
```

## 同步与复制
### TiDB Data Migration工具
```
1、全量和增量同步，异步。从mysql协议数据库同步到TiDB
2、根据binlog同步，可以表过滤、表操作过滤（如只针对insert语句）
3、可针对分库分表合并，可以异构表迁移（列不一样）
4、集群部署，dmctl控制，dm-master调度，dm-worker执行任务
```

### TiDB Binlog工具
```
1、5.0后被TiCDC替代
2、全量同步，逻辑备份。从tidb-server获取binlog row模式日志实现同步，下游可以是kafka、TiDB、类mysql、本地文件
3、集群部署，binlogctl控制，Pump记录binlog并按时间排序，再由Drainer将日志归并转换为结构化数据传给下游
4、日志只记录提交的事务，按提交时间，只有增删改没有查日志
```

### TiDB CDC工具
```
1、增量同步，异步复制。从TiKV获取变更日志，下游是类mysql和kafka
2、集群部署，各节点的capture组件部分拉取（默认并发），master节点合并排序
3、限制：表必须有索引
```