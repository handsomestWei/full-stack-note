package com.wjy.geotool.demo.swing;

import java.io.File;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

public class SwingMap {
    public static void main(String[] args) throws Exception {
        // 选择一个shp文件
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        // 加载shp图层数据源
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        // 创建一个地图容器
        MapContent map = new MapContent();
        map.setTitle("Quickstart");

        // 创建一个简单的样式，并将样式和shp数据源加载到一个图层上
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);

        // 在地图容器添加图层
        map.addLayer(layer);

        // 展示地图
        JMapFrame.showMap(map);
    }
}
