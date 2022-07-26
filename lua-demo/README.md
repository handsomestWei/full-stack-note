# lua-demo
lua demo

## 环境搭建
[下载地址](http://luabinaries.sourceforge.net/download.html)
linux环境下
```
yum install lua
```
安装后验证
```
lua -v
```

## 脚本执行
```
lua xxx.lua
```

## nginx整合lua
nginx需要添加lua模块

### 嵌入内容
```
修改nginx.conf如下
location /lua {
    default_type 'text/plain';
    content_by_lua 'ngx.say("hello,lua")';
}
```

### 嵌入lua脚本
```
1、在nginx目录下创建脚本目录
2、放入脚本文件
3、修改nginx.conf如下
    location /lua {
        #lua_code_cache off;
        content_by_lua_file lua/hello-nginx.lua;
    }
4、访问/lua，页面展示脚本输出内容
```

### 调试lua脚本
```
1、nginx.conf放开注释lua_code_cache off，即关闭lua脚本缓存，修改脚本内容不需重启nginx实现热更新，但会影响性能。
2、使用ZeroBrane Studio(https://studio.zerobrane.com/)远程或本地模拟调试
```

### 查看lua报错日志
```
在nginx中使用lua报错时，页面提示500 Internal Server Error。查看lua报错信息需修改nginx.conf
vim /usr/local/nginx/conf/nginx.conf
```
```
日志级别改为debug
error_log /home/wwwlogs/nginx_error.log debug;
在nginx_error.log里可以看到lua报错信息
```

### 调试数据
```
nginx+lua有时不方便直接在显示页面里呈现内容，特别是在线上，可以采用输出头信息来达到调试数据的目的，可以使用ngx.header来设置头信息输出。例如：
ngx.header["xxx"] = "xxx"
```

### nginx lua api
nginx提供api给lua调用，[参考](https://www.nginx.com/resources/wiki/modules/lua/#nginx-api-for-lua)
```
除了content_by_lua_file等指令外，还能使用lua脚本连接redis、mysql等
```