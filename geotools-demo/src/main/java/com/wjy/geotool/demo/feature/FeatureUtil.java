package com.wjy.geotool.demo.feature;

import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

// 创建点线面要素
public class FeatureUtil {

    // 创建点
    public List<SimpleFeature> createPoint() throws Exception {
        List<SimpleFeature> list = new ArrayList();

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureType featureType = FeatureTypeUtil.createPointFeatureTypeByStr();
        // SimpleFeatureType featureType = FeatureTypeUtil.createPointFeatureTypeByBuilder();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        for (int i = 0; i < 10; i++) {
            // 创建geometry
            Coordinate coordinate = new Coordinate(100 + i, 30 + i);
            Point point = geometryFactory.createPoint(coordinate);
            // 创建feature
            featureBuilder.set("the_geom", point);
            featureBuilder.set("name", "名称-" + i);
            featureBuilder.set("number", i);
            SimpleFeature feature = featureBuilder.buildFeature(null);
            list.add(feature);
        }
        return list;
    }

    // 创建线
    public SimpleFeature createLine() throws Exception {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureType featureType = FeatureTypeUtil.createLineStringFeatureTypeByStr();
        // SimpleFeatureType featureType = FeatureTypeUtil.createLineStringFeatureTypeByBuilder();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        // 构建线geometry
        List<Coordinate> coordList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Coordinate coordinate = new Coordinate(100 + i, 30 + i);
            coordList.add(coordinate);
        }
        Coordinate[] array = coordList.toArray(new Coordinate[] {});
        LineString lineString = geometryFactory.createLineString(array);

        // 创建feature
        featureBuilder.set("the_geom", lineString);
        featureBuilder.set("name", "名称");
        featureBuilder.set("number", 10);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        return feature;
    }

    // 测试
    public static void main(String[] args) throws Exception {
        FeatureUtil featureUtil = new FeatureUtil();

        List<SimpleFeature> list = featureUtil.createPoint();
        for (SimpleFeature simpleFeature : list) {
            List<Object> attributes = simpleFeature.getAttributes();
            System.out.println(attributes);
        }

        SimpleFeature line = featureUtil.createLine();
        List<Object> attributes = line.getAttributes();
        System.out.println(attributes);
    }
}
