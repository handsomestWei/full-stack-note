# win-DL
Windows下安装CUDA和Pytorch跑深度学习DeepLearning。

## CUDA安装
CUDA是Nvidia GPU的并行计算框架，一个专门与通用程序而不是图形程序对接的库，提供便捷API计算工具      
```
假设算100000次从1加到10000000
CPU（假设4个线程）：要算100000/4=250000次
GPU（假设1000个线程）：要算1000000/1000=1000次
CUDA：能提供一种类似高斯“1加到50，利用首尾相加再除以2”的方法来简化计算，那么使用CUDA后的NV显卡可能只需要计算200次，效率提高了很多。
```
[下载地址](https://developer.nvidia.com/zh-cn/cuda-downloads)   
安装后查看gpu使用情况
```
nvidia-smi
```

## miniconda安装
miniconda是python环境管理工具，包含conda软件包管理器和Python   
[下载地址](https://docs.conda.io/en/latest/miniconda.html)  [国内镜像](https://mirrors.tuna.tsinghua.edu.cn/anaconda/miniconda/)   
安装时勾选加入环境变量
```
python --version
```
### pip install 默认安装路径修改
```
## 1、查看默认安装路径
python -m site

其中:
USER_BASE python.exe启动程序路径
USER_SITE 依赖安装包基础路径

## 2、查看对应配置文件
python -m site -help

## 3、进入site.py配置文件，修改USER_SITE和USER_BASE
```

## pytorch安装
PyTorch是使用GPU和CPU优化的深度学习张量库，基于Torch。可以看作加入了GPU支持的numpy。[下载地址](https://pytorch.org/get-started/locally/)   [中文文档](https://pytorch-cn.readthedocs.io/zh/latest/)   
```
pip3 install torch==1.8.1+cu111 torchvision==0.9.1+cu111 torchaudio===0.8.1 -f https://download.pytorch.org/whl/torch_stable.html
```
    
## jupyter安装
Jupyter Notebook是一个交互式笔记本，支持运行 40 多种编程语言。文件后缀为.ipynb，能将代码、文档等这一切集中到一处。本质是一个Web应用程序。
```
pip install jupyter d2l
```
进入[动手学深度学习官网](https://zh-v2.d2l.ai/)选择首页的`jupyter记事本`下载d2l-zh
### 运行笔记本
```
jupyter notebook
```
### 运行实例
打开下载到本地的d2l-zh jupyter文档，并运行
```
xxx/d2l-zh/pytorch/chapter_convolutional-modern/resnet.ipynb
```