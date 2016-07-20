package com.agar.view;

import com.agar.Invoker;
import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.data.JsonConstraint;
import com.agar.data.JsonModelImport;
import com.agar.factory.DaoFactory;
import com.agar.model.Constraint;
import com.agar.model.ModelImport;
import com.agar.utils.Utils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import java.io.IOException;
import java.util.*;

/**
 * Created by SDEV2 on 29/06/2016.
 */
public class TableViewControl extends TableView<String> implements Subscriber
{
    private /*List*/Set<ModelImport> listModelImport;
    private String modelImportJsonFileName;
    private String constraintJsonFileName;
    private Map<String, List<ObservableValue<Boolean>>> map = new HashMap<>();
    private List<Constraint> listConstraint;
    private TableColumn<String, Boolean> constraintColumn;

    /**
     * @param modelImportJsonFileName
     * @param constraintJsonFileName
     * @param modelImportName
     */
    public TableViewControl(String modelImportJsonFileName,String constraintJsonFileName, String modelImportName) throws IOException {
        this.modelImportJsonFileName = Objects.requireNonNull(modelImportJsonFileName, "The Json file name can't be null");
        this.constraintJsonFileName = Objects.requireNonNull(constraintJsonFileName, "The Json file name can't be null");
        this.setEditable(true);
        if( modelImportName == null)
            this.setVisible(false);
        init(modelImportName);
    }

    /**
     * @param modelImportName
     * @throws IOException
     */
    public void init(String modelImportName) throws IOException {
        JsonModelImport.getInstance().register(this);
        setItems(modelImportName);
    }

    public void loadData(String modelImportJsonFileName,String constraintJsonFileName) throws IOException {
        this.listModelImport = JsonModelImport.getInstance().getModelImports(modelImportJsonFileName);
        this.listConstraint = JsonConstraint.getInstance().getConstraints(constraintJsonFileName);
        //Invoker.getInstance().invoke(Utils.Cmd.GET_MODEL_IMPORT);
    }

    /**
     *
     */
    public void addColumns(){
        TableColumn<String, String> fieldColumn = new TableColumn<>("");
        fieldColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        this.getColumns().add(fieldColumn);

        int i = 0;
        for(Constraint constraint : listConstraint){
            constraintColumn = new TableColumn<>(constraint.getName());
            constraintColumn.setCellFactory(CheckBoxTableCell.forTableColumn(constraintColumn));
            constraintColumn.setEditable(true);
            int j = i;
            constraintColumn.setCellValueFactory(cellData -> map.get(cellData.getValue()).get(j));
            this.getColumns().add(constraintColumn);
            i++;
        }
    }

    /**
     * @param modelImportName
     */
    public void setItems(String modelImportName) throws IOException {
        long size = 0;
        String databaseName = DaoFactory.getDatabaseName();
        ModelImport selectedModelImport = null;

        if(modelImportName != null && databaseName != null)
        {
            loadData(this.modelImportJsonFileName, this.constraintJsonFileName);
            clear();
            boolean modelImportNameFound = false;

            for (ModelImport modelImport : listModelImport) {
                if(modelImport.getDatabaseName().contentEquals(databaseName)){
                    selectedModelImport = modelImport;
                }
                if(selectedModelImport != null){
                    for(ModelImport.Component component : selectedModelImport.getComponents()){
                        for(String key : component.getModel().keySet()){
                            if (key.contains(modelImportName)) {
                                modelImportNameFound = true;
                                size = component.getModel().get(modelImportName).stream().count();
                                component.getModel().get(modelImportName).stream()
                                        .forEach(item -> {
                                            this.getItems().add(item);
                                            map.put(item, new ArrayList<>());
                                            listConstraint.forEach(constraint -> map.get(item).add(new SimpleBooleanProperty(false)));
                                        });
                                break;
                            }
                        }
                    }
                }
                    /*for (String key : modelImport.getModel().keySet()) {
                        if (key.contains(modelImportName)) {
                            modelImportNameFound = true;
                            size = modelImport.getModel().get(modelImportName).stream().count();
                            modelImport.getModel().get(modelImportName).stream()
                                    .forEach(item -> {
                                        this.getItems().add(item);
                                        map.put(item, new ArrayList<>());
                                        listConstraint.forEach(constraint -> map.get(item).add(new SimpleBooleanProperty(false)));
                                    });
                            break;
                        }
                    }*/
                if (modelImportNameFound) break;
            }
            if(size > 0){
                addColumns();
                this.setVisible(true);
            }
            else
                this.setVisible(false);
        }
        else
            this.setVisible(false);
    }

    /**
     * @param subject
     */
    @Override
    public void update(Subject subject) throws IOException {
        this.setItems((String) subject.getValue());
    }

    /**
     *
     */
    public void clear(){
        this.getItems().clear();
        this.map.clear();
        this.getColumns().clear();
    }

}
