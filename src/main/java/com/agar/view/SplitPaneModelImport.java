package com.agar.view;

import com.agar.data.JsonModelImport;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class SplitPaneModelImport extends SplitPane
{
    private ListViewModelImport listViewModelImport;

    public SplitPaneModelImport(String JsonFileName)
    {
        VBox vBox = new VBox();
        listViewModelImport = new ListViewModelImport(JsonFileName);
        HBox hBox = new HBox();
        TextField newModel = new TextField();
        newModel.setPromptText("Inserez le nouveau model");
        Button addModelImportBTN = new Button("Nouveau");
        hBox.getChildren().addAll(newModel, addModelImportBTN);

        addModelImportBTN.setOnAction(event -> {
            if(JsonModelImport.addModelImport(JsonFileName, newModel.getText(), null))
                listViewModelImport.addItem(newModel.getText());
        });

        vBox.getChildren().addAll(listViewModelImport,  hBox);
        this.getItems().addAll(vBox);
    }

    public ListViewModelImport getListViewComponent(){
        return listViewModelImport;
    }
}
