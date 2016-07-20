package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.factory.DaoFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by SDEV2 on 11/07/2016.
 */

public class DBTablesTree extends TreeView<String> implements Subject {

    private ObservableMap<String, Set<String>> observableMap;
    private ObservableMap<String, Set<String>> selectedObservableMap = FXCollections.observableMap(new TreeMap<>());
    private CheckBoxTreeItem<String> superRootItem = new CheckBoxTreeItem<>(DaoFactory.getDatabaseName()+" (base de données)");
    private DBTablesTree.Status status = Status.CANDIDATE;
    private List<Subscriber> subscribers = new ArrayList<>();
    private Info value = null;

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

        this.setOnMouseClicked(event -> {
                if (this.getSelectionModel().getSelectedItem() != null) {
                    if (this.getStatus() == Status.SELECTED)
                        this.setValue(this.getSelectionModel().getSelectedItem().getValue());
                }
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void register(Subscriber subscriber) {
        if(!subscribers.contains(subscriber))subscribers.add(subscriber);
    }

    @Override
    public void unregister(Subscriber subscriber) {
        if(subscribers.contains(subscriber))subscribers.remove(subscriber);
    }

    @Override
    public boolean isAttached(Subscriber subscriber) {
        return subscribers.contains(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> {
            try {
                subscriber.update(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setValue(Object object) {
        this.value = (Info) object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return value;
    }

    enum Status{
        CANDIDATE, SELECTED
    }

    enum Type{
        TABLE, FIELD
    }

    static class Info{
        String value;
        Type type;

        Info(){}

        public Info(String value, Type type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }
}
