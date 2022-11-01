package com.wjy.geotool.demo.crs;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

// crs实现不同坐标系的坐标转换
public class CrsUtil {

    public void crsTransform() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        // 通常逻辑上理解经度应该是横坐标x，纬度是y，可是这里经度要填到y，纬度x，否则会报错
        Coordinate coordinate = new Coordinate(30, 100);
        Point point = geometryFactory.createPoint(coordinate);

        CoordinateReferenceSystem crs1 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crs2 = CRS.decode("EPSG:3857");
        // allow for some error due to different datums
        boolean lenient = true;
        MathTransform transform = CRS.findMathTransform(crs1, crs2, lenient);
        Geometry geometry = JTS.transform(point, transform);
        System.out.println(geometry);
    }

    // 测试
    public static void main(String[] args) throws Exception {
        CrsUtil crsUtil = new CrsUtil();
        crsUtil.crsTransform();
    }
}
