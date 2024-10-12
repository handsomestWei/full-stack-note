# maven-dep-demo
自定义spring boot项目maven工程依赖管理示例。

## 简介
自定义parent和dependency，整合开发中常用到的spring-boot-dependencies和其他私有依赖，利用maven的dependencyManagement做统一版本管理。

## 使用说明
工程构建成果为.pom，上传到maven私有仓库。
### 本地maven配置
使用时修改maven的settings.xml文件：   
1、在```<profiles>```节点新增一组```<profile>```配置。
```
<profile>
	<id>ctm-release</id>
	<repositories>
		<repository>
			<id>ctm-release</id>
			<name>ctm-release</name>
			<url>xxx</url>
			<releases>
				<enabled>true</enabled>
			</releases>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>
<profile>       
```
2、在```<activeProfiles>```节点新增```<activeProfile>```配置。
```
<activeProfile>ctm-release</activeProfile>
```

### 使用方式
#### 方式一：继承（推荐）
1、在项目根pom，将原parent spring-boot-starter-parent改为本工程。
```
<parent>
    <groupId>com.xxx</groupId>
    <artifactId>ctm-spring-boot-starter-parent</artifactId>
    <version>${last.version.RELEASE}</version>
</parent>
```
2、删除项目原```<dependencyManagement>```节点   
3、在```<dependencies>```节点中引用依赖时，不要显式声明依赖的版本号，会自动继承```<dependencyManagement>```节点中声明的。在多模块项目中，对子模块也适用。

#### 方式二：组合
原项目已经继承私有parent（非原生spring-boot-starter-parent），本工程提供有dependency支持组合的方式使用。   
1、在项目根pom的```<dependencyManagement>```节点，添加以下配置
```
<dependency>
    <groupId>com.xxx</groupId>
    <artifactId>ctm-spring-boot-dependencies</artifactId>
    <version>${last.version.RELEASE}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```
```   
注：若版本控制失效，一般是父级私有parent内也声明有一份dependencyManagement，导致子的失效。在本级需要把引用type pom的方式，改为显式声明，手动复制所有dependency到dependencyManagement节点内。
```
2、在```<dependencies>```节点中引用依赖，不用带版本号。

## 附maven标签功能简介
### dependencyManagement简介
[官网简介链接](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)。依赖包版本的管理器，dependencyManagement里只是声明依赖，并不实现引入。
+ 版本控制：如果dependencies里的dependency没有声明version元素，那么maven就会到dependencyManagement里面去找有没有对该artifactId和groupId进行过版本声明，如果有，就继承它。
+ 中断：如果dependencies中的dependency声明了version，那么无论dependencyManagement中有无对该jar的version声明，都以dependency里的version为准。   

在引用```<type>pom</type>```类型时，一般结合```<scope>import</scope>```一起使用。

### type pom简介
dependency的type类型默认是jar。当项目中需要引入很多jar包，容易导致单个pom.xml文件过大，可读性降低。可以单独定义一个type类型为pom的maven项目，做依赖包统一管理，不需要到处定义。使用时直接引用，利用maven依赖传递特性。