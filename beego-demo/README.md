# beego-demo
golang web框架beego使用

## bee常用命令
### 创建项目
```
bee new [projectName]
```
### 生成model
```
bee generate model [modelname] [-fields=""]   
例：bee generate model biz -fields="Id:int64,Name:string"
```
### 生成controller
```
bee generate controller [controllerfile]   
例：bee generate controller biz
```
### 生成api文档
可通过swagger UI来展示
```
bee generate docs
```
### api调试
```
bee run -gendoc=true -downdoc=true
```

## 接口调试
需先生成api文档
```
访问http://ip:port/swagger/
```
## 性能监控
```
配置EnableAdmin = true
访问http://ip:8088/
```

## 测试
### 单元测试
```
go test [package] -cover   
例：go test test   
或cd到测试目录 go test
```
### 测试覆盖率报告
```
go test [package] -cover -coverprofile=c.out   
go tool cover -html=c.out -o coverage.html
```

## 参考
[beego官方文档](https://beego.me/docs/intro/) 