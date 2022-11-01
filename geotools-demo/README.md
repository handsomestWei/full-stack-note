# geotools-demo
geotools使用demo

## geotools简介
```
Geotools是一个符合OGC标准（也称为OpenGIS标准）的Java类库，它提供了很多的标准类来处理空间数据。OpenGIS标准是一个空间数据处理的国际标准。目前的大部分基于Java的开源GIS软件，如udig，geoserver等，都是调用GeoTools库来进行空间数据的处理。
```
```
GeoTools是一个处理空间数据的工具包，其中包含构建一个完整的地理信息系统所需要的的全部工具类，常见的问题比如投影的转换、基准面的设定、空间数据对象的渲染和样式、各种数据源的支持、各种GIS文件格式支持、空间数据过滤与搜索等，都有对应的解决方案，这些解决方案被封装成一个一个的类，可供用户调用。
```
### 核心工具包

#### GeoAPI
根据OpenGIS规范而开发一组Java接口，这组接口代表了OpenGIS规范，GeoTools实现了GeoAPI中的接口，从而也就是实现了OpenGIS标准。

#### JTS
JTS（Java Topology Suite），java拓扑套件，提供集成几何支持。

### 插件和扩展机制
用户可以为GeoTools做插件和扩展。如果希望GeoTools支持更多的数据格式和其他的坐标系统，就可以为GeoTools开发相应的插件。

## 参考
[geotools官网](https://www.geotools.org/)   
[geotools quickstart](http://docs.geotools.org/latest/userguide/tutorial/quickstart/intellij.html)
