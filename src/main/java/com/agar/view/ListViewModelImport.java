package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.data.JsonModelImport;
import com.agar.model.ModelImport;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;

import java.io.IOException;
import java.util.*;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class ListViewModelImport extends ListView<String> implements Subject {

    private Map<String, ObservableValue<Boolean>> map = new /*HashMap*/TreeMap<>();
    private String selectedModel = null;
    private List<Subscriber> subscribers = new ArrayList<>();

    public ListViewModelImport(String JsonFileName) throws IOException {
            this.setEditable(true);
            this.setPrefSize(200, 300);
            init(JsonFileName);
    }

    public void init(String JsonFileName) throws IOException {
        try {
            for(ModelImport modelImport : JsonModelImport.getModelImports(JsonFileName)) {
                for(String modelName : modelImport.getModel().keySet())
                    map.put(modelName, new SimpleBooleanProperty(false));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        refresh();
        this.getItems().addAll(map.keySet());
        this.setCellFactory(CheckBoxListCell.forListView(item -> map.get(item)));
    }

    private void uncheckTheOtherModel(String selectedModel)
    {
        map.forEach((key, value) -> {if(!key.contentEquals(selectedModel))map.replace(key, value, new SimpleBooleanProperty(false));});
        this.getItems().clear();
        refresh();
        this.getItems().addAll(map.keySet());
    }

    public void addItem(String item){
        this.map.put(item, new SimpleBooleanProperty(false));
        this.map.get(item).addListener((observable, oldValue, newValue) -> {if(newValue)uncheckTheOtherModel(item);});
        this.getItems().add(item);
    }

    public void refresh(){
        map.forEach((key, value) -> value.addListener((observable, oldValue, newValue) -> {if(newValue){this.setValue(key);uncheckTheOtherModel(key);}else this.setValue(null);}));
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
    public void notifySubscribers() {
        subscribers.forEach(subscriber->subscriber.update(this));
    }

    @Override
    public void setValue(Object object) {
        this.selectedModel = (String)object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return this.selectedModel;
    }
}
