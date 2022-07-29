# maven-plugin-demo

## exec-maven-plugin
用org.codehaus.mojo的插件exec-maven-plugin执行程序。

### 使用
在pom.xml中定义，可以执行shell命令、构建docker镜像、用npm打包（pom文件和前端工程放在maven项目一个子模块里）等。
```
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>x.y.z</version>
    ... ...
<plugin>
```