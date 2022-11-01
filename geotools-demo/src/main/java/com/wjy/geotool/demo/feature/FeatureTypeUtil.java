package com.wjy.geotool.demo.feature;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

// 创建要素类型
public class FeatureTypeUtil {

    // 通过字符串创建 点要素类型
    public static SimpleFeatureType createPointFeatureTypeByStr() throws SchemaException {
        String typeStr = "the_geom:Point:srid=4326,name:String,number:Integer";
        return DataUtilities.createType("typeName", typeStr);
    }

    // 通过字符串创建 线要素类型
    public static SimpleFeatureType createLineStringFeatureTypeByStr() throws SchemaException {
        String typeStr = "the_geom:LineString:srid=4326,name:String,number:Integer";
        return DataUtilities.createType("typeName", typeStr);
    }

    // 通过字符串创建 面要素类型
    public static SimpleFeatureType createPolygonFeatureTypeByStr() throws SchemaException {
        String typeStr = "the_geom:Polygon:srid=4326,name:String,number:Integer";
        return DataUtilities.createType("typeName", typeStr);
    }

    // 通过SimpleFeatureTypeBuilder创建 点要素类型
    public static SimpleFeatureType createPointFeatureTypeByBuilder() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("typeName2");
        builder.setCRS(crs);
        builder.add("the_geom", Point.class);
        builder.length(15).add("name", String.class);
        builder.add("number", Integer.class);
        return builder.buildFeatureType();
    }

    // 通过SimpleFeatureTypeBuilder创建 线要素类型
    public static SimpleFeatureType createLineStringFeatureTypeByBuilder() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("typeName2");
        builder.setCRS(crs);
        builder.add("the_geom", LineString.class);
        builder.length(15).add("name", String.class);
        builder.add("number", Integer.class);
        return builder.buildFeatureType();
    }

    // 通过SimpleFeatureTypeBuilder创建 面要素类型
    public static SimpleFeatureType createPolygonFeatureTypeByBuilder() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("typeName2");
        builder.setCRS(crs);
        builder.add("the_geom", Polygon.class);
        builder.length(15).add("name", String.class);
        builder.add("number", Integer.class);
        return builder.buildFeatureType();
    }

    // 测试
    public static void main(String[] args) throws Exception {
        SimpleFeatureType featureType1 = FeatureTypeUtil.createPointFeatureTypeByStr();
        System.out.println(featureType1);
        SimpleFeatureType featureType2 = FeatureTypeUtil.createLineStringFeatureTypeByStr();
        System.out.println(featureType2);
        SimpleFeatureType featureType3 = FeatureTypeUtil.createPolygonFeatureTypeByStr();
        System.out.println(featureType3);

        SimpleFeatureType featureType11 = FeatureTypeUtil.createPointFeatureTypeByBuilder();
        System.out.println(featureType11);
        SimpleFeatureType featureType22 = FeatureTypeUtil.createLineStringFeatureTypeByBuilder();
        System.out.println(featureType22);
        SimpleFeatureType featureType33 = FeatureTypeUtil.createPolygonFeatureTypeByBuilder();
        System.out.println(featureType33);
    }
}
