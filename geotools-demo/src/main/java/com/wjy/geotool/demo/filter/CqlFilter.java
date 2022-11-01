package com.wjy.geotool.demo.filter;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

// https://blog.csdn.net/sipengfei_/article/details/112463864
public class CqlFilter {

    public void cqlFilter() throws Exception {
        // 单条件过滤
        Filter filter = CQL.toFilter("number >= 10");
        // 多条件过滤
        List<Filter> filters = CQL.toFilterList("name = 'test'; number>10");
        // between过滤
        Filter filter1 = CQL.toFilter("number between 10 and 20");
        // 几何关系过滤
        // Filter result = CQL.toFilter("CONTAINS(ATTR1, POINT(1 2))" );
        // Filter result = CQL.toFilter("BBOX(ATTR1, 10,20,30,40)" );
        // Filter result = CQL.toFilter("DWITHIN(ATTR1, POINT(1 2), 10, kilometers)" );
        // Filter result = CQL.toFilter("CROSS(ATTR1, LINESTRING(1 2, 10 15))" );
        // Filter result = CQL.toFilter("INTERSECT(ATTR1, GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15
        // 15, 20 20)) )" );
        // Filter result = CQL.toFilter("CROSSES(ATTR1, LINESTRING(1 2, 10 15))" );
        // Filter result = CQL.toFilter("INTERSECTS(ATTR1, GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING
        // (15 15, 20 20)) )" );
    }

    // 测试几何关系：shape文件中的图形与自定义图形之间的空间几何关系
    public static void main(String[] args) throws Exception {
        // 属性过滤
        // Filter filter = CQL.toFilter("name='名称-6' and number>5");

        // 矩形范围bbox过滤
        // Filter filter = CQL.toFilter("BBOX(the_geom, 103,30,105,35)");

        // 圆形范围过滤
        // CoordinateUtil coordinateUtil = new CoordinateUtil();
        // Polygon circle = coordinateUtil.createCircleByCoordinate();
        // Filter filter = CQL.toFilter("INTERSECTS(the_geom, "+circle+")" );

        // 多边形范围过滤
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate1 = new Coordinate(100, 30);
        Coordinate coordinate2 = new Coordinate(105, 30);
        Coordinate coordinate3 = new Coordinate(103, 35);
        Coordinate[] coordinates = new Coordinate[] {coordinate1, coordinate2, coordinate3, coordinate1};
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        LinearRing holes[] = null;
        Polygon polygon = geometryFactory.createPolygon(linearRing, holes);
        Filter filter = CQL.toFilter("INTERSECTS(the_geom, " + polygon + ")");

        File shapeFile = new File("E:\\temp\\temp\\test.shp");
        ShapefileDataStore dataStore = new ShapefileDataStore(shapeFile.toURI().toURL());
        dataStore.setCharset(Charset.forName("UTF-8"));
        String type = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(type);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures(filter);
        FeatureIterator<SimpleFeature> iterator = collection.features();
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            List<Object> attributes = feature.getAttributes();
            System.out.println(attributes);
        }
        iterator.close();
    }
}
