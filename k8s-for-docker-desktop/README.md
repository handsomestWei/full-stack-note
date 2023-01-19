# k8s-for-docker-desktop
在win10环境使用docker-desktop安装部署k8s和istio

## 版本
+ docker-desktop 3.0.0，选择[1.19.3分支](https://github.com/AliyunContainerService/k8s-for-docker-desktop/tree/v1.19.3)，docker-desktop版本过高在win10企业版安装失败，提示需要更高的win版本。
+ k8s 1.19.3
+ istio 1.5.0

## 安装部署记录
+ 用cmd和shell执行k8s dashboard看板生成token命令失败。在win power shell上执行命令解决。
+ docker-desktop k8s一直starting问题。查看docker-desktop日志无error，删除C:\Users\{用户名}\.kube文件夹解决。原因是之前安装卸载过其他版本docker-desktop残留有信息。
+ 使用istioctl命令安装提示不识别set标记。虽然istioctl.exe已加入Path环境变量。直接使用istioctl.exe执行安装命令解决。
+ istio使用。查看istio监控，可使用istioctl d kiali命令，将会打开本地浏览器，标记d是dashboad缩写，还能打开controlz、envoy、jaeger、grafana等组件的看板。示例位于samples文件夹内，可kubectl apply -f samples/xxx/xxx.yaml部署体验其他功能。


## 参考
[k8s-for-docker-desktop](https://github.com/AliyunContainerService/k8s-for-docker-desktop)