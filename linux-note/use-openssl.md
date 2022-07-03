# use-openssl
openssl使用

# Usage
## 提取密钥对
数字证书pfx包含公钥和私钥，而cer证书只包含公钥。提取需输入证书保护密码
```
openssl pkcs12 -in xxx.pfx -nocerts -nodes -out pare.key
```

## 提取私钥
```
openssl rsa -in pare.key -out pri.key
```

## 提取公钥
```
openssl rsa -in pare.key -pubout -out pub.key
```

## 私钥转换
转换为java用的pkcs8格式
```
openssl pkcs8 -topk8 -inform PEM -in pri.key -outform pem -nocrypt -out pri.pkcs8.key
```

## 验证
私钥加密，公钥解密
```
openssl rsautl -sign -in data.txt -inkey pri.key -out enc.bin
openssl rsautl -verify -in enc.bin -inkey pub.key -pubin -out dec.txt
```

