# vue-note
vue入门

### npm安装
npm是Nodejs下的包管理器，因此需要
[下载nodejs](http://nodejs.cn/download/)
```
npm -v
```
安装cnpm，中国npm镜像的客户端
```
npm install -g cnpm --registry=https://registry.npm.taobao.org
## 升级npm
## cnpm install -g npm
```
安装webpack，通过CommonJS的语法把所有浏览器端需要发布的静态资源做相应的准备，比如资源的合并和打包

### vue-cli安装
vue的命令行工具
```
cnpm install -g vue-cli
vue -V
```

### 使用vue-cli创建项目
```
vue init webpack <projectName>
```