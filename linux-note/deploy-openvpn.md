# deploy-openvpn
centos搭建openvpn服务

# Usage
## 服务端
### 依赖组件
```
yum -y install epel-release openvpn easy-rsa net-tools bridge-utils
```

### 服务端证书
创建ca和证书，使用免密模式
```
cd /usr/share/easy-rsa/3
./easyrsa init-pki
./easyrsa build-ca
./easyrsa build-server-full server1 nopass
./easyrsa build-client-full client1 nopass
./easyrsa gen-dh
openvpn --genkey --secret ./pki/ta.key
```
复制证书到运行目录下
```
cp -pR /usr/share/easy-rsa/3/pki/{issued,private,ca.crt,dh.pem,ta.key} /etc/openvpn/server/
```

### 配置路由转发
修改内核参数
```
vi /etc/sysctl.d/99-sysctl.conf
配置net.ipv4.ip_forward = 1
sysctl --system 
```

### 服务端配置
配置文件里的路径相对server.conf所在目录
```
cp /usr/share/doc/openvpn-x/sample/sample-config-files/server.conf /etc/openvpn/server/
vi /etc/openvpn/server/server.conf
```
<details>
<summary>服务端配置说明</summary>
<pre><code>## 为客户端分配的ip段
server 10.8.0.0 255.255.255.0
## 服务端内网网段例172.16.30.0则配置如下
push "route 172.16.30.0 255.255.255.0"
## 允许多客户端使用同一个证书连接
duplicate-cn
## 证书目录
ca ca.crt
cert issued/server1.crt
key private/server1.key
dh dh.pem
## 日志输出目录
status /var/log/openvpn/openvpn-status.log
log    /var/log/openvpn/openvpn.log
## 使用tcp时注释掉以下配置
;explicit-exit-notify 1
</code></pre>
</details>

### 服务端启动
```
systemctl start openvpn-server@server
systemctl enable openvpn-server@server
systemctl status openvpn-server@server
```
### 配置允许访问同一网段
不配置则客户端只能访问vpn服务器所在ip。需添加路由表配置转发，例10.8.0.0/16是服务端server.conf为客户端分配的网段
```
iptables -t nat -A POSTROUTING -s 10.8.0.0/16 -j MASQUERADE
```

### 查看服务端日志
详见服务端server.conf里配置的日志目录

### 查看已接入的客户端
```
cat /etc/openvpn/server/ipp.txt
```

## 客户端
### 客户端证书
拷贝服务端生成的证书文件放到客户端config目录下
```
/etc/openvpn/server/ca.crt
/etc/openvpn/server/ta.key
/etc/openvpn/server/issued/client1.crt
/etc/openvpn/server/private/client1.key  
```
### windows客户端配置
从服务端拷贝sample.ovpn配置编辑，也放在config目录下
```
/usr/share/doc/openvpn-x/sample/sample-windows/sample.ovpn
```
<details>
<summary>客户端配置说明</summary>
<pre><code>## vpn服务端所在公网ip和vpn服务端口
remote x.x.x.x 1194
## 客户端证书
ca ca.crt
cert client1.crt
key client1.key 
tls-auth ta.key 1
## 设备类型和服务端配置一致
dev tun
## 协议要和服务端配置一致
proto tcp
</code></pre>
</details>