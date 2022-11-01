package com.wjy.geotool.demo.feature;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

// 通过coordinate创建点线面geometry
public class CoordinateUtil {

    // 通过coordinate创建点
    public Point createPointByCoordinete() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate = new Coordinate(100, 30);
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }

    // 通过coordinate创建多点
    public MultiPoint createMultiPointByCoordinete() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate1 = new Coordinate(100, 30);
        Coordinate coordinate2 = new Coordinate(110, 30);
        Coordinate coordinate3 = new Coordinate(120, 30);
        Coordinate[] coordinates = new Coordinate[] {coordinate1, coordinate2, coordinate3};
        MultiPoint point = geometryFactory.createMultiPoint(coordinates);
        return point;
    }

    // 通过coordinate创建线
    public LineString createLineStringByCoordinete() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate1 = new Coordinate(100, 30);
        Coordinate coordinate2 = new Coordinate(110, 30);
        Coordinate coordinate3 = new Coordinate(120, 30);
        Coordinate[] coordinates = new Coordinate[] {coordinate1, coordinate2, coordinate3};
        LineString lineString = geometryFactory.createLineString(coordinates);
        return lineString;
    }

    // 通过coordinate创建多线
    public MultiLineString createMultiLineStringByCoordinete() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate1 = new Coordinate(100, 30);
        Coordinate coordinate2 = new Coordinate(110, 30);
        Coordinate coordinate3 = new Coordinate(120, 30);
        Coordinate[] coordinates = new Coordinate[] {coordinate1, coordinate2, coordinate3};

        LineString lineString = geometryFactory.createLineString(coordinates);
        Coordinate coordinate4 = new Coordinate(110, 40);
        Coordinate coordinate5 = new Coordinate(120, 40);
        Coordinate[] coordinates2 = new Coordinate[] {coordinate4, coordinate5};

        LineString lineString2 = geometryFactory.createLineString(coordinates2);
        LineString[] lineStrings = new LineString[] {lineString, lineString2};
        MultiLineString multiLineString = geometryFactory.createMultiLineString(lineStrings);
        return multiLineString;
    }

    // 通过coordinate创建面
    public Polygon createPolygonByCoordinate() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate1 = new Coordinate(100, 30);
        Coordinate coordinate2 = new Coordinate(110, 30);
        Coordinate coordinate3 = new Coordinate(105, 35);
        Coordinate[] coordinates = new Coordinate[] {coordinate1, coordinate2, coordinate3, coordinate1};
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        LinearRing holes[] = null;
        Polygon polygon = geometryFactory.createPolygon(linearRing, holes);
        return polygon;
    }

    // 通过coordinate创建圆
    public Polygon createCircleByCoordinate() throws Exception {
        // GeometryBuilder builder = new GeometryBuilder();
        // // 经度,纬度,半径(经纬度),边的数量
        // Polygon circle = builder.circle(110, 30, 10, 32);
        // 计算转换坐标系的转换矩阵
        CoordinateReferenceSystem crs1 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crs2 = CRS.decode("EPSG:3857");
        MathTransform transform = CRS.findMathTransform(crs1, crs2, true);
        MathTransform transform2 = CRS.findMathTransform(crs2, crs1, true);
        // 转换中心点
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate center = new Coordinate(30, 100);
        Point point = geometryFactory.createPoint(center);
        Point point2 = (Point)JTS.transform(point, transform);
        // 计算缓冲区(缓冲区半径10km)
        Polygon buffer3857 = (Polygon)point2.buffer(10);
        // 转换缓冲区
        Polygon buffer4326 = (Polygon)JTS.transform(buffer3857, transform2);
        // 调整缓冲区的经纬度顺序
        Coordinate[] coordinates = buffer4326.getCoordinates();
        List<Coordinate> coords = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i++) {
            coords.add(new Coordinate(coordinates[i].y, coordinates[i].x));
        }
        LinearRing linearRing = geometryFactory.createLinearRing(coords.toArray(new Coordinate[] {}));
        Polygon polygon = geometryFactory.createPolygon(linearRing, null);
        return polygon;
    }

    // 测试
    public static void main(String[] args) throws Exception {
        CoordinateUtil coordinateUtil = new CoordinateUtil();

        Point point = coordinateUtil.createPointByCoordinete();
        System.out.println(point);

        MultiPoint multiPoint = coordinateUtil.createMultiPointByCoordinete();
        System.out.println(multiPoint);

        LineString lineString = coordinateUtil.createLineStringByCoordinete();
        System.out.println(lineString);

        MultiLineString multiLineString = coordinateUtil.createMultiLineStringByCoordinete();
        System.out.println(multiLineString);

        Polygon polygon = coordinateUtil.createPolygonByCoordinate();
        System.out.println(polygon);

        Polygon circle = coordinateUtil.createCircleByCoordinate();
        System.out.println(circle);
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate center = new Coordinate(100.05, 30);
        Point point0 = geometryFactory.createPoint(center);
        System.out.println(circle.contains(point0));
    }
}
