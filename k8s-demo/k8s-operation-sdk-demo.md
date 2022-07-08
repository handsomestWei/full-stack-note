# k8s-operation-sdk-demo
k8s crd，使用[operation-sdk脚手架](https://github.com/operator-framework/operator-sdk/releases)，制作k8s operator应用。

## operation-sdk使用
```
operator-sdk init simple-k8s-operator --repo github.com/kubernetes/sample-controller
operator-sdk create api --group test --version v1beta1 --kind Test
operator-sdk create webhook --group test --version v1beta1 --kind Test --defaulting --programmatic-validation
```

## 参考
[operator hub](https://operatorhub.io/operator/)