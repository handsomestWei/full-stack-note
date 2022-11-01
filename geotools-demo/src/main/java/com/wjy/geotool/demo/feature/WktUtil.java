package com.wjy.geotool.demo.feature;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

// 通过wkt创建点线面geometry
public class WktUtil {

    // 通过wkt创建点
    public Point createPointByWkt() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        Point point = (Point)wktReader.read("POINT(100 30)");
        return point;
    }

    // 通过wkt创建多点
    public MultiPoint createMultiPointByWkt() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        MultiPoint point = (MultiPoint)wktReader.read("MULTIPOINT(100 30)");
        return point;
    }

    // 通过wkt创建线
    public LineString createLineStringByWkt() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        LineString lineString = (LineString)wktReader.read("LINESTRING(100 30, 110 35, 120 40)");
        return lineString;
    }

    // 通过wkt创建多线
    public MultiLineString createMultiLineStringByWkt() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        MultiLineString multiLineString =
            (MultiLineString)wktReader.read("MULTILINESTRING((100 30, 110 35),(120 40, 125 40))");
        return multiLineString;
    }

    // 通过wkt创建面
    public Polygon createPolygonByWkt() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        // 里面的小括号个数可以有多个，第一个被后面的那些挖洞
        // Polygon polygon = (Polygon)wktReader.read("POLYGON((100 30,115 35,113 35))");
        Polygon polygon =
            (Polygon)wktReader.read("POLYGON((100 30,115 35,113 35, 100 30),(111 30, 112 30, 112 32, 111 30))");
        return polygon;
    }

    // 测试
    public static void main(String[] args) throws Exception {
        WktUtil wktUtil = new WktUtil();

        Point point = wktUtil.createPointByWkt();
        System.out.println(point);

        MultiPoint multiPoint = wktUtil.createMultiPointByWkt();
        System.out.println(multiPoint);

        LineString lineString = wktUtil.createLineStringByWkt();
        System.out.println(lineString);

        MultiLineString multiLineString = wktUtil.createMultiLineStringByWkt();
        System.out.println(multiLineString);

        Polygon polygon = wktUtil.createPolygonByWkt();
        System.out.println(polygon);
    }
}
