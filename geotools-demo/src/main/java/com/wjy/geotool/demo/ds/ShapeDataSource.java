package com.wjy.geotool.demo.ds;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.wjy.geotool.demo.feature.FeatureTypeUtil;
import com.wjy.geotool.demo.feature.FeatureUtil;

// 操作shape数据源
public class ShapeDataSource {

    // 读取shape文件
    public void readShapeFile(File shapeFile) throws Exception {
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(shapeFile.toURI().toURL());
        shapefileDataStore.setCharset(Charset.forName("UTF-8"));
        String typeName = shapefileDataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = shapefileDataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
        FeatureIterator<SimpleFeature> iterator = collection.features();
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            List<Object> attributes = feature.getAttributes();
            System.out.println(attributes);
        }
        iterator.close();
    }

    // 写入shape文件
    public void writeShapeFile(File shapeFile) throws Exception {
        // 创建要素
        SimpleFeatureType featureType = FeatureTypeUtil.createPointFeatureTypeByStr();
        FeatureUtil featureUtil = new FeatureUtil();
        List<SimpleFeature> list = featureUtil.createPoint();
        SimpleFeatureCollection collection = new ListFeatureCollection(featureType, list);
        // 构建参数，创建ShapefileDataStore
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", shapeFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        ShapefileDataStore dataStore = (ShapefileDataStore)dataStoreFactory.createNewDataStore(params);
        dataStore.createSchema(featureType);
        dataStore.setCharset(Charset.forName("UTF-8"));
        // 写入要素
        Transaction transaction = new DefaultTransaction("create");
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore)featureSource;
            featureStore.setTransaction(transaction);
            featureStore.addFeatures(collection);
            transaction.commit();
        }
    }

    // 测试
    public static void main(String[] args) throws Exception {
        File file = new File("E:\\temp\\temp\\test.shp");
        ShapeDataSource shapeDatasource = new ShapeDataSource();
        shapeDatasource.writeShapeFile(file);
        shapeDatasource.readShapeFile(file);
    }
}
