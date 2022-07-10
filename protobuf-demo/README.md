# protobuf-demo
protobuf入门

**protobuf**与**xml**和**json**相比   
+ 优点：传输效率快；序列化后体积小适合网络传输；支持跨平台多语言。
+ 缺点：二进制格式导致可读性差；缺乏自描述。

# Usage

## 定义protobuf结构
```
参考\src\main\resources\proto
```
## proto文件转java类
### 转换工具下载
[下载protoc.exe](https://github.com/protocolbuffers/protobuf/releases)   
[源码protobuf](https://github.com/protocolbuffers/protobuf/tree/master/java)

### maven插件配置
[插件配置](https://www.xolstice.org/protobuf-maven-plugin/compile-mojo.html)
```
<plugin>
	<groupId>org.xolstice.maven.plugins</groupId>
	<artifactId>protobuf-maven-plugin</artifactId>
	<version>0.6.1</version>
</plugin>
```
### 执行转换
```
方式一：执行generated-proto.bat，使用protoc工具将.proto文件转换为.java文件
方式二：执行mvn protobuf:compile -DprotocExecutable="xxx/bin/protoc.exe"
```

## 消息转换
```
使用org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
```

## 序列化和反序列化
```
执行com.wjy.demo.protobuf.test.ProtobufDemoApplicationTests
```
