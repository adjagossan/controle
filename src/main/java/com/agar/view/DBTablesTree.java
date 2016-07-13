package com.agar.view;

import com.agar.factory.DaoFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTree extends TreeView<String> {
    private ObservableMap<String, List<String>> observableMap;
    private ObservableMap<String, List<String>> selectedObservableMap = FXCollections.observableMap(new TreeMap<>());
    private CheckBoxTreeItem<String> superRootItem = new CheckBoxTreeItem<>(DaoFactory.getDatabaseName()+" (base de données)");

    public DBTablesTree(TreeMap<String, List<String>> observableMap) throws SQLException, ClassNotFoundException {
        this.superRootItem.setExpanded(true);
        this.setItems(observableMap);
        this.setCellFactory(CheckBoxTreeCell.forTreeView());
        this.setRoot(superRootItem);
        init();
    }

    public void init(){
        this.observableMap.forEach((s, strings) -> {
            CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(s);
            this.superRootItem.getChildren().add(rootItem);
            strings.forEach(s1 -> {
                CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(s1);
                rootItem.getChildren().add(item);
                item.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue){
                        if(selectedObservableMap.containsKey(item.getParent().getValue())){
                            selectedObservableMap.get(item.getParent().getValue()).add(item.getValue());
                        }
                        else{
                            selectedObservableMap.put(item.getParent().getValue(), new ArrayList<>());
                            selectedObservableMap.get(item.getParent().getValue()).add(item.getValue());
                        }
                        selectedObservableMap.forEach((s2, strings1) -> System.out.println(s2+" **-> "+String.join(",",strings1)));
                        System.out.println("*****size: "+selectedObservableMap.size());
                    }
                    else{
                        int size = selectedObservableMap.get(item.getParent().getValue()).size();
                        if(size == 1)
                            selectedObservableMap.remove(item.getParent().getValue());
                        else if(size > 1)
                            selectedObservableMap.get(item.getParent().getValue()).remove(item.getValue());
                        selectedObservableMap.forEach((s2, strings1) -> System.out.println(s2+" **-> "+String.join(",",strings1)));
                        System.out.println("*****size: "+selectedObservableMap.size());
                    }
                });
            });
        });
    }

    public void setItems(TreeMap<String, List<String>> map){
        this.observableMap = FXCollections.observableMap(map);
    }

    public void setItems(ObservableMap<String, List<String>> observableMap){
        this.observableMap = observableMap;
        this.superRootItem.getChildren().clear();
        this.init();
    }

    public ObservableMap<String, List<String>> getItems(){
        return this.observableMap;
    }

    public ObservableMap<String, List<String>> getSelectedObservableMap() {
        return selectedObservableMap;
    }

    public void remove(){

    }
}
