package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.data.JsonModelImport;
import com.agar.model.ModelImport;
import com.agar.utils.Utils;
import com.agar.view.alert.ExceptionHandler;
import com.agar.view.dialog.TextInputDialog;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.*;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class ListViewModelImport extends ListView<String> implements Subject {

    private Map<String, ObservableValue<Boolean>> map = new /*HashMap*/TreeMap<>();
    private String selectedModel = null;
    private static ListViewModelImport listViewModelImport;

    private ListViewModelImport(){}

    public static String getCheckedModel() {
        return checkedModel;
    }

    private void setCheckedModel(String checkedModel) {
        this.checkedModel = checkedModel;
    }

    private static String checkedModel = null;
    private List<Subscriber> subscribers = new ArrayList<>();

    private ListViewModelImport(String JsonFileName) throws IOException {
            this.setEditable(true);
            this.setPrefSize(200, 300);
            init(JsonFileName);
    }

    public static ListViewModelImport getInstance(String JsonFileName) throws IOException {
        if(listViewModelImport == null)
            listViewModelImport = new ListViewModelImport(JsonFileName);
        return listViewModelImport;
    }

    public static ListViewModelImport getInstance(){
        return listViewModelImport != null ? listViewModelImport : null;
    }

    public void init(String JsonFileName) throws IOException {
        try {
            for(ModelImport modelImport : JsonModelImport.getInstance().getModelImports(JsonFileName)) {
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

        //contextMenuHandler();
    }
/*
    private void contextMenuHandler(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addField = new MenuItem("Ajouter un nouveau champ");
        addField.setOnAction(event -> {
            String selectedModel = this.getSelectionModel().getSelectedItem();
            new TextInputDialog(selectedModel, "Champ", "Entrez un nouveau champ")
                    .showAndWait()
                    .ifPresent(s -> {
                        try {
                            JsonModelImport.getInstance().addModelImport(Utils.modelImportJsonFileName, selectedModel, s);
                        } catch (IOException e) {
                            e.printStackTrace();
                            new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
                        }
                    });
        });
        contextMenu.getItems().add(addField);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton().equals(MouseButton.SECONDARY) && !this.getSelectionModel().isEmpty()) contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });
    }
*/
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
        map.forEach((key, value) -> value.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.setValue(key);
                this.setCheckedModel(key);
                uncheckTheOtherModel(key);
            }
            else this.setValue(null);
        }));
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
        this.selectedModel = (String)object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return this.selectedModel;
    }
}
