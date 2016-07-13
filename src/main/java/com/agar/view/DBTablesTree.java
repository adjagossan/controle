package com.agar.view;

import com.agar.factory.DaoFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTree extends TreeView<String> {
    private ObservableMap<String, Set<String>> observableMap;
    private ObservableMap<String, Set<String>> selectedObservableMap = FXCollections.observableMap(new TreeMap<>());
    private CheckBoxTreeItem<String> superRootItem = new CheckBoxTreeItem<>(DaoFactory.getDatabaseName()+" (base de données)");

    public DBTablesTree(TreeMap<String, Set<String>> observableMap) throws SQLException, ClassNotFoundException {
        this.superRootItem.setExpanded(true);
        this.setItems(observableMap);
        this.setCellFactory(CheckBoxTreeCell.forTreeView());
        this.setRoot(superRootItem);
        this.setShowRoot(false);
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
                            selectedObservableMap.put(item.getParent().getValue(), new HashSet<>());
                            selectedObservableMap.get(item.getParent().getValue()).add(item.getValue());
                        }
                    }
                    else{
                        int size = selectedObservableMap.get(item.getParent().getValue()).size();
                        if(size == 1)
                            selectedObservableMap.remove(item.getParent().getValue());
                        else if(size > 1)
                            selectedObservableMap.get(item.getParent().getValue()).remove(item.getValue());
                    }
                });
            });
        });
    }

    public void setItems(TreeMap<String, Set<String>> map){
        this.observableMap = FXCollections.observableMap(map);
    }

    public void setItems(ObservableMap<String, Set<String>> observableMap){
        observableMap.forEach((s, strings) -> {
            if(!this.observableMap.containsKey(s))
                this.observableMap.put(s, new HashSet<>());
            strings.forEach(s1 -> this.observableMap.get(s).add(s1));
        });
        refresh();
    }

    public void refresh(){
        this.superRootItem.getChildren().clear();
        this.init();
    }

    public ObservableMap<String, Set<String>> getItems(){
        return this.observableMap;
    }

    public ObservableMap<String, Set<String>> getSelectedObservableMap() {
        return selectedObservableMap;
    }

    public void remove(ObservableMap<String, Set<String>> selectedObservableMap){
        selectedObservableMap.forEach((s, strings) -> {
            if( this.observableMap.containsKey(s)){
                if(strings.size() == this.observableMap.get(s).size())
                    this.observableMap.remove(s, strings);
                else if(this.observableMap.get(s).size() == 1)
                    this.observableMap.remove(s);
                else
                    strings.forEach(s1 -> this.observableMap.get(s).remove(s1));
            }
        });
        refresh();
    }
}
