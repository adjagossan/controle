package com.agar.view;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTree extends TreeView<String> {
    private Map<String, List<String>> map = new HashMap<>();

    public DBTablesTree(Map<String, List<String>> map){
        if(map.size() > 1)
            throw new IllegalArgumentException("one Table at once");
        this.map = map;
        init();
    }
/*
    public void setItems(Map<String, List<String>> map){
        this.map = map;
    }*/

    public void init(){
        map.forEach((s, strings) -> {
            CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(s);
            this.setRoot(rootItem);
            rootItem.setExpanded(true);
            strings.forEach(s1 -> rootItem.getChildren().add(new CheckBoxTreeItem<>(s1)));
        });
    }
}
