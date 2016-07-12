package com.agar.view;

import com.agar.dao.DatabaseTableDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTree extends TreeView<String> {
   // private TreeMap<String, List<String>> map = new TreeMap<>();
    private ObservableMap<String, List<String>> observableMap;
    private CheckBoxTreeItem<String> superRootItem = new CheckBoxTreeItem<>();
    //private DatabaseTableDao dao;

    public DBTablesTree(TreeMap<String, List<String>> observableMap) throws SQLException, ClassNotFoundException {
        this.superRootItem.setExpanded(true);
        //this.dao = new DatabaseTableDao();
        //this.map = dao.getTablesWithAssociatedColumns();
        //FXCollections.observableMap(map);
        this.setItems(observableMap);
        this.setCellFactory(CheckBoxTreeCell.forTreeView());
        this.setRoot(superRootItem);
        init();
    }


    public void init(){
        this.observableMap.forEach((s, strings) -> {
            CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(s);
            this.superRootItem.getChildren().add(rootItem);
            //rootItem.setExpanded(true);
            strings.forEach(s1 -> rootItem.getChildren().add(new CheckBoxTreeItem<>(s1)));
        });

    }

    public void setItems(TreeMap<String, List<String>> map){
        //this.map = map;
        this.observableMap = FXCollections.observableMap(map);
    }

    public ObservableMap<String, List<String>> getItems(){
        return this.observableMap;
    }
}
