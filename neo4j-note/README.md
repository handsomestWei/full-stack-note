# neo4j-note
neo4j图数据库笔记

## 图原生-无索引邻接
```
 neo4j存储数据，也存储关系。在遍历关系时，原生的Neo4j图数据库中只要找到起始节点、读取节点的邻接边就可以访问该节点的邻居；而无需像关系数据库那样需要执行昂贵的连接JOIN操作，系统开销大大减少、执行效率极大提升，被称作“无需索引的邻接关系遍历”(Index Free Adjacency)。在关系型数据库中，关系是“计算”出来的；而在Neo4j图数据库中，关系是“读”出来的。
```
```
 与原生图数据库相对应的是“非原生”或者“多模式”图数据库。这些数据库支持图的表示和遍历，查询语言常采用Gremlin、或者类似SQL的语言；其底层物理存储则是键-值对，或者基于列的存储，或者关系存储。非原生图数据库由于受到底层存储模式的限制，在处理多层遍历(例如搜索某节点的3阶以上的邻居)时，其性能往往会受到影响。
```

## 下载
```
社区版下载地址https://neo4j.com/download-center/#community
java8环境选择下载3.x版本
```

## 安装
```
添加环境变量NEO4J_HOME，配置为安装包解压目录路径
添加到Path环境里，配置为%NEO4J_HOME%/bin
```

## 运行
```
/xxx/bin/neo4j.bat console
访问数据库http://localhost:7474，默认账号密码neo4j
运行电影的知识图谱例子：在界面输入:play movie graph，按教程创建数据。

沙盒环境https://sandbox.neo4j.com/
```

## Java开发
```
<!-- 使用官方组件http://neo4j.com.cn/public/docs/chapter4/index.html -->
<dependency> 
   <groupId>org.neo4j</groupId> 
   <artifactId>neo4j</artifactId> 
   <version>1.8</version> 
</dependency> 

<!-- jpa方式，利用相关注解 -->
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-neo4j</artifactId>
</dependency>

或是使用mybatis-plus，在mapper xml里编写cypher语句
```