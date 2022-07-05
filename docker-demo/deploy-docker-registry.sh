#!bin/bash

docker image pull registry

mkdir /data/registry/data
mkdir /data/registry/certs
cd /data/registry/certs
openssl req -newkey rsa:4096 -nodes -sha256 -keyout ca.key -x509 -days 365 -out ca.crt -subj \
"/C=CN/ST=GX/L=Bei Jing/O=xxx Technology Co., Ltd./CN=xxx Root Certificate"
openssl req -newkey rsa:4096 -nodes -sha256 -keyout registry.key -out registry.csr -subj \
"/C=CN/ST=GX/L=Bei Jing/O=xxxx Technology Co., Ltd./CN=registry.xxx.com"
openssl x509 -req -days 365 -in registry.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out registry.crt

cat /data/registry/certs/ca.crt >> /etc/pki/tls/certs/ca-bundle.crt　　
systemctl restart docker
echo 'x.x.x.x registry.xxx.com' >> /etc/hosts/hosts

docker run -d -p 5000:5000 --restart=always --name registry \
-v /data/registry/data:/var/lib/registry \
-v /data/registry/certs:/certs \
-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/registry.crt \
-e REGISTRY_HTTP_TLS_KEY=/certs/registry.key \
registry:2

docker ps -a | grep 'registry:2'