# postgis-note
postgresql数据库gis扩展postgis使用笔记。

## 简介
PostGIS是在对象关系型数据库PostgreSQL上增加了存储管理空间数据的能力的开源GIS数据库。依托于PostgreSQL的两个重要特性：Geometry对象、Gist索引。

## Geometry对象
Geometry（几何对象类型）是PG的一个基本存储类型，PostGIS的空间数据都会以Geometry的形式存储在PostgreSQL里，本质是个二进制对象。
```
geometrystr保存多边形各点坐标位置，在页面上各点连线形成图斑展示。例：
POLYGON ((13798650 4826450, 13812600 4827600, 13798100 4813550, 13802450 4813800, 13807600 4814350, 13804700 4813000, 13811400 4812900, 13813400 4817500, 13810000 4818350, 13813400 4813900, 13811750 4820300, 13807500 4828350, 13805000 4828000, 13802750 4827650, 13798650 4826450))
```

## Gist索引
Gist(Generalized Search Tree)，通用搜索树，本身是索引框架。PG针对Geometry对象已经写好了一套Gist索引，用于空间检索。空间索引不像B树索引那样提供精确的结果，而是提供近似的结果。Gist的索引建立依赖于聚合运算（聚合运算的实现接口PG也开放出来了，用户甚至可以自己写），适合多维数据类型和集合数据类型。

## 地理坐标系GCS
SRS：空间参照系，SRID：空间参照标识符。在gis数据库定义Geometry类型时需传入SRID指定坐标系类型。不同的坐标系数据直接不能混用，需要额外使用函数做转换。

## 投影坐标系PGS
在平面地图上绘制数据需要PCS。在GIS中，PGS是依赖于GCS的，投影是用一定的数据算法如高斯投影将球体坐标转换成平面坐标。

## 并行计算
修改$PGDATA/postgresql.conf配置文件，配置并行地理计算，加速空间查询，优化性能。

## 地图纠偏
服务商提供的地图一般出于数据安全考虑，经纬度故意出现偏差，需要使用纠偏算法修正。

## 高程模型
数字高程模型（Digital Elevation Model)，简称DEM，是通过有限的地形高程数据实现对地面地形的数字化模拟。引入高程模型可以更加准确的计算出位置信息。

## POI
Point of interesting。地图上任何非地理意义的有意义的点：比如商店，医院，车站等。

## web gis
### Shapefile
在gis软件出现之前，平面文件Shapefile一直是空间数据存储和交互的标准数据格式。

### 地图瓦片
地图瓦片是包含了一系列比例尺、一定地图范围内的地图切片文件。主要使用两种方式，一种是传统的栅格瓦片，另外一种是新出的矢量瓦片（Vector Tiles）。

### 栅格瓦片
栅格瓦片提前渲染，对客户端性能要求低，但瓦片占用空间高，无法动态调整地图比例。

### 矢量瓦片
矢量切片技术继承了矢量数据和切片地图的双重优势。   
mapbox是通用的矢量瓦片数据标准。   
传输时MIME类型应该设置为application/vnd.mapbox-vector-tile    
矢量切片常见的形式右：GeoJSON（.json文件）、TopoJSON和MapBox Vector Tile（.mvt文件）。

#### GeoJSON
GeoJSON是一种对各种地理数据结构进行编码的格式，基于Javascript对象表示法的地理空间信息数据交换格式。以文本形式来表示不同地理空间。可以使用GeoTools将GeoJson文件导入到PostGis表的Geometry类型字段。

#### MVT
.mvt矢量瓦片压缩率更高，体积更小。没有经纬度概念，保存的是格网相对坐标，来构建几何图形信息。也可用Google的Protocol Buffer对.mvt文件进行编码压缩为二进制生成.pbf文件

#### 动态矢量瓦片
动态矢量瓦片不再使用工具线下进行预先切片，采用即时浏览即时传输矢量瓦片，提升大规模空间数据的前端渲染流畅度。
```
MapBox GL是MapBox提供的JavaScript SDK。请求路径例：
http://ip:port/getMvt/{z}/{x}/{y}
```
```
后台根据接收到的z、x、y这三个值计算对应切片的边界范围，可以使用postgis的ST_AsMVT聚合函数返回该范围的mvt(Mapbox Vector Tiles) 二进制矢量瓦片给前端。
SELECT ST_AsMVT(mvtgeom.*)
FROM (
  SELECT ST_AsMVTGeom(geom,bound) AS geom, column1, column2
  FROM myTable
) mvtgeom
```

#### 数据加密
保护瓦片数据安全。

## 三维gis

## 地图服务器
### OSM
开放街道图（OpenStreetMap，简称OSM）是一个网上地图协作计划，一般用.osm文件存储地图信息。

### OGC
OGC开放地理空间信息联盟(Open Geospatial Consortium)，制定了数据和服务的一系列标准，GIS厂商按照这个标准进行开发可保证空间数据的互操作。

### 地图服务分类
对于矢量切片，目前OGC没有提供这样的服务。但是像GeoServer等地图服务器提供了矢量切片的扩展，可以用工具对自己的矢量数据进行切片，然后导出GeoJSON格式的数据加载到地图中。      
WMS服务，图片格式的地图web map service。实时渲染，通常用作预览数据。   
WFS服务，网络要素服务web feature service，使用矢量图形。   
WMTS服务，网络地图切片服务web map tile service，对图片进行切片后的服务。   
TMS服务，tiled map service，瓦片地图服务。

### 地图底图和图层
地图内是由各种不同的图层来叠加显示，形成整个三维地图场景。包括道路网、河道、行政区划等。可以使用工具绘制、提取、添加自定义图层。实践中，地图的正常的组织形式为：地图=底图图层+行业数据叠加层。

### GeoServer地图服务
是OpenGIS Web服务器规范的J2EE实现，利用GeoServer可以方便的发布地图数据，可以将地图数据切成各种地图瓦片。

### osm2pgsql
[安装](https://osm2pgsql.org/download/windows/)使用osm2pgsql工具，将[osm地图](http://download.geofabrik.de/asia.html)文件导入PostGIS数据库，会自动按图层生成图层表。

### GeoTools
开源的Java GIS工具包，可利用它来开发符合标准的地理信息系统。GeoTools提供了OGC规范的一个实现来作为他们的开发。

## 大数据gis
[HBase Ganos](https://help.aliyun.com/document_detail/87287.html)是阿里云的时空大数据引擎系统。接口数据返回GeoJSON格式

## 参考
[知乎PostGIS教程](https://zhuanlan.zhihu.com/p/62034688)   
[使用switch2osm搭建OSM地图服务](https://switch2osm.org/serving-tiles/)   
[OSM地图本地发布](https://www.jianshu.com/p/a4831d84220b)   
[阿里云数据可视化平台](http://datav.aliyun.com/portal/school/atlas/area_selector)