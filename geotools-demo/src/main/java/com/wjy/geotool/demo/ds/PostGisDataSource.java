package com.wjy.geotool.demo.ds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

// 连接postgis数据源，读取数据
public class PostGisDataSource {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap();
        // 需要连接何种数据库 postgis
        params.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
        // ip地址
        params.put(PostgisNGDataStoreFactory.HOST.key, "127.0.0.1");
        // 端口号
        params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
        // 需要连接的数据库名
        params.put(PostgisNGDataStoreFactory.DATABASE.key, "sdc");
        // 架构
        params.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
        // 用户名
        params.put(PostgisNGDataStoreFactory.USER.key, "postgres");
        // 密码
        params.put(PostgisNGDataStoreFactory.PASSWD.key, "123456");
        try {
            // 获取存储空间
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            // 根据表名获取source
            SimpleFeatureSource featureSource = dataStore.getFeatureSource("city");
            SimpleFeatureCollection features = featureSource.getFeatures();
            SimpleFeatureIterator iterator = features.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // 获取 wkt feature
                Object geometry = feature.getDefaultGeometry();
                System.out.println(geometry.toString());
                // 获取属性列表
                List<Object> attributes = feature.getAttributes();
                System.out.println(attributes.toString());
            }
            iterator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
