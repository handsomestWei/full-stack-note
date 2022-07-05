# deploy-docker-registry
centos搭建docker registry镜像仓库

# Usage
## 拉取镜像
```
docker image pull registry
```

## 证书配置
创建镜像仓库的镜像数据目录和证书目录，便于挂载
```
mkdir /data/registry/data
mkdir /data/registry/certs
```
生成镜像仓库证书
```
cd /data/registry/certs
openssl req -newkey rsa:4096 -nodes -sha256 -keyout ca.key -x509 -days 365 -out ca.crt -subj \
"/C=CN/ST=GX/L=Bei Jing/O=xxx Technology Co., Ltd./CN=xxx Root Certificate"
openssl req -newkey rsa:4096 -nodes -sha256 -keyout registry.key -out registry.csr -subj \
"/C=CN/ST=GX/L=Bei Jing/O=xxxx Technology Co., Ltd./CN=registry.xxx.com"
openssl x509 -req -days 365 -in registry.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out registry.crt
```
ssl配置，将自签名证书加入系统证书中心，使操作系统信任
```
## 对于ubuntu系统则是/etc/ssl/certs/ca-certificates.crt
cat /data/registry/certs/ca.crt >> /etc/pki/tls/certs/ca-bundle.crt
systemctl restart docker
```

## 仓库运行
```
docker run -d -p 5000:5000 --restart=always --name registry \
-v /data/registry/data:/var/lib/registry \
-v /data/registry/certs:/certs \
-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/registry.crt \
-e REGISTRY_HTTP_TLS_KEY=/certs/registry.key \
registry:2
```

## 客户端访问
使用registry.xxx.com:5000访问，配置镜像仓库的域名解析
```
echo 'x.x.x.x registry.xxx.com' >> /etc/hosts/hosts
```

## 镜像迁移
镜像从旧仓库导出
```
docker tag registry:2 registry.xxx.com:5000/public/registry:2
docker save -o registry.tar registry.xxx.com:5000/public/registry:2
```
镜像导入到新镜像仓库
```
docker load -i registry.tar
```
镜像推送到新镜像仓库
```
docker push registry.xxx.com:5000/public/registry:2
```
也可以直接把整个镜像仓库数据文件挂载到新仓库
```
-v /data/registry/data:/var/lib/registry
```

# 参考
[docker registry官方文档](https://docs.docker.com/registry/deploying/#run-an-externally-accessible-registry)