package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.data.JsonConstraint;
import com.agar.data.JsonModelImport;
import com.agar.model.Constraint;
import com.agar.model.ModelImport;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import java.util.List;

/**
 * Created by SDEV2 on 29/06/2016.
 */
public class TableViewControl extends TableView<String> implements Subscriber
{
    private List<ModelImport> listModelImport;
    /**
     *
     * @param modelImportJsonFileName
     * @param constraintJsonFileName
     * @param modelImportName
     */
    public TableViewControl(String modelImportJsonFileName,String constraintJsonFileName, String modelImportName)
    {
        this.setEditable(true);
        if( modelImportName == null)
            this.setVisible(false);
        init(modelImportJsonFileName, constraintJsonFileName, modelImportName);
    }

    public void init(String modelImportJsonFileName,String constraintJsonFileName, String modelImportName)
    {
        TableColumn<String, String> fieldColumn = new TableColumn<>("");
        fieldColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        this.getColumns().add(fieldColumn);

        listModelImport = JsonModelImport.getModelImports(modelImportJsonFileName);
        setItems(modelImportName);
        List<Constraint> listConstraint = JsonConstraint.getConstraints(constraintJsonFileName);
        TableColumn<String, Boolean> constraintColumn;

        for(Constraint constraint : listConstraint)
        {
            constraintColumn = new TableColumn<>(constraint.getName());
            constraintColumn.setCellFactory(CheckBoxTableCell.forTableColumn(constraintColumn));
            constraintColumn.setEditable(true);
            constraintColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(false));
            this.getColumns().add(constraintColumn);
        }
    }

    public void setItems(String modelImportName)
    {
        long size = 0;
        if(modelImportName != null)
        {
            this.getItems().clear();
            boolean modelImportNameFound = false;
            for (ModelImport modelImport : listModelImport) {
                for (String key : modelImport.getModel().keySet()) {
                    if (key.contains(modelImportName)) {
                        modelImportNameFound = true;
                        size =  modelImport.getModel().get(modelImportName).stream().count();
                        modelImport.getModel().get(modelImportName).stream().forEach(item -> this.getItems().add(item));
                        break;
                    }
                }
                if (modelImportNameFound) break;
            }
            if(size > 0)
                this.setVisible(true);
            else
                this.setVisible(false);
        }
        else
            this.setVisible(false);
    }

    @Override
    public void update(Subject subject) {
        this.setItems((String) subject.getValue());
    }
}
