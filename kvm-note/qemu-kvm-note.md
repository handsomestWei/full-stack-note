# qemu-kvm-note
qemu-kvm使用笔记

## kvm和qemu关系
+ kvm是linux的一个模块，是工作在cou硬件支持基础之上的虚拟化技术。加载该模块后，才能进一步通过其他工具创建虚拟机。
+ 仅有kvm模块，用户无法直接控制内核模块，必须有一个用户空间的工具，例如qemu，是一个虚拟化软件。

## qemu-kvm使用
### 安装
```
$ yum install -y qemu-kvm
$ ln -sv /usr/libexec/qemu-kvm /usr/local/bin/
```

### 模拟cpu
使用-cpu指定模拟的cpu
```
kvm-qemu -cpu ?
```

### 创建centos虚拟机
qemu-img是QEMU的磁盘管理工具。
```
## 创建虚拟磁盘映像
qemu-img create -o size=5G,preallocation=metadata -f qcow2 centos-mini.qcow2

## 以创建好的磁盘映像启动虚拟机
qemu-kvm -m 1024 -smp 2 -name 'centos-mini' \
> -drive file=/root/images/centos-mini.qcow2,if=virtio,media=disk,format=qcow2,cache=writeback \
> -drive file=/root/iso/CentOS-7-x86_64-Minimal-1810.iso,media=cdrom -boot order=dc,once=d \
> --vnc 0.0.0.0:0

## 连上vnc开始安装
vncviewer ip:5900

## 安装完成启动虚拟机
qemu-kvm -m 1024 -smp 2 -name 'centos-mini' \
> -drive file=/root/images/centos-mini.qcow2,if=virtio,media=disk,format=qcow2,cache=writeback
> -nographic
```