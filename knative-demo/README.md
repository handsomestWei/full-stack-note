# knative-demo
[knative-samples](https://github.com/knative/docs/blob/main/docs/samples/serving.md)   
主要特性：符合条件时自动扩缩容。比如长时间没有流量时副本数自动缩容到0

## 简介
相对于serverful，serverless=FaaS+BaaS。knative是一套serverless架构方案，构建在k8s和istio之上。


## 扩缩容配置
### KPA
Knative Pod Autoscaler基于请求的自动扩缩容功能   
查看配置
```
kubectl -n knative-serving get cm config-autoscaler
```
默认配置
```
apiVersion: v1
kind: ConfigMap
metadata:
 name: config-autoscaler
 namespace: knative-serving
data:
 container-concurrency-target-default: 100
 container-concurrency-target-percentage: 1.0
 enable-scale-to-zero: true
 enable-vertical-pod-autoscaling: false
 max-scale-up-rate: 10
 panic-window: 6s
 scale-to-zero-grace-period: 30s
 stable-window: 60s
 tick-interval: 2s
```
### k8s HPA
Horizontal POD Autoscaler基于CPU的自动扩缩容功能
```
spec:
  template:
    metadata:
      autoscaling.knative.dev/metric: concurrency
      autoscaling.knative.dev/class: hpa.autoscaling.knative.dev
```