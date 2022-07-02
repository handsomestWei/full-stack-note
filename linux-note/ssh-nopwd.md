# ssh-nopwd
ssh免密登录配置

# Usage
## 生成密钥
```
ssh-keygen -t rsa -C 'comment'
```
参数说明
```
-t rsa: 指定使用 rsa加密
-C: 添加备注方便区分, 备注会加到公钥文件最后
```
命令会有交互输入，使用默认值回车，在根目录的.ssh/文件夹下生成四个文件
```
authorized_keys: 存放远程免密登录的公钥
id_rsa: 生成的私钥文件
id_rsa.pub: 生成的公钥文件
known_hosts: 已知的主机公钥清单
```
## 密钥分发
复制本机公钥到目标机器上
```
sh-copy-id -i ~/.ssh/id_rsa.pub user_name@ip
```
## 主机名配置
用hostname代替ip地址更容易记住
```
cat <<EOF>>/etc/hosts
ip hostname
EOF
```

## 免密登录
```
ssh user_name@ip
ssh user_name@hostname
```