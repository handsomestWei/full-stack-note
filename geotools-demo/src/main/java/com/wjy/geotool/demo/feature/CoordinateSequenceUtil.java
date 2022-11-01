package com.wjy.geotool.demo.feature;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import it.geosolutions.jaiext.jts.CoordinateSequence2D;

// 通过CoordinateSequence创建点线面geometry
public class CoordinateSequenceUtil {

    // 通过CoordinateSequence创建点
    public Point createPointByCoordinateSequence() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        CoordinateSequence2D coordinateSequence = new CoordinateSequence2D(100, 30);
        Point point = geometryFactory.createPoint(coordinateSequence);
        return point;
    }

    // 通过CoordinateSequence创建多点
    public MultiPoint createMultiPointByCoordinateSequence() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        CoordinateSequence2D coordinateSequence = new CoordinateSequence2D(100, 30, 110, 35);
        MultiPoint multiPoint = geometryFactory.createMultiPoint(coordinateSequence);
        return multiPoint;
    }

    // 通过CoordinateSequence创建线
    public LineString createLineStringByCoordinateSequence() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        CoordinateSequence2D coordinateSequence = new CoordinateSequence2D(100, 30, 110, 35, 120, 40);
        LineString lineString = geometryFactory.createLineString(coordinateSequence);
        return lineString;
    }
}
