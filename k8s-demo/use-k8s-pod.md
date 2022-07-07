# use-k8s-pod
优雅的操作容器组

# Usage
## 回退备份
```
kubectl get deploy deployName -o yaml>>deployName-bak-`date "+%Y-%m-%d"`.yaml
```

## 获取副本数
```
replicas=`echo | kubectl get -o template deploy/deployName --template={{.spec.replicas}}`
```

## 停止容器组
```
kubectl scale deployment deployName --replicas=0
```

## 修改配置
```
kubectl set env deploy/deployName privateCertPath="/var/www/cert/xxx.key"
```

## 替换内容
```
kubectl get deploy deployName -o yaml | sed "s#cert#cert-new#g" | kubectl replace -f -
```

## 增加内容
```
cat <<EOF > ./patch-file.yaml
spec:
  template:
    spec:
      containers:
      - name: patch-demo
        image: redis
EOF
kubectl patch deployment deployName --patch "$(cat patch-file.yaml)"
```

## 恢复容器组
```
kubectl scale deployment trade-service --replicas=$replicas
```

## 验证比较
```
kubectl get deploy deployName -oyaml>>deployName-new-`date "+%Y-%m-%d"`.yaml
kubectl diff --filename="deployName-new-`date "+%Y-%m-%d"`.yaml,deployName-bak-`date "+%Y-%m-%d"`.yaml"
```

## 回退
```
kubectl apply -f deployName-bak-`date "+%Y-%m-%d"`.yaml
```

## 其他
### 操作secret数据卷
```
kubectl delete secret secretName
kubectl create secret generic secretName --from-file=./xxx.key --from-file=./xx.cer --from-file=./xxx.cer
```