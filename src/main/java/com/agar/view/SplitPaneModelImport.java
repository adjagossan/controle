package com.agar.view;

import com.agar.data.JsonModelImport;
import com.agar.view.alert.ExceptionHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class SplitPaneModelImport extends SplitPane
{
    private ListViewModelImport listViewModelImport;

    public SplitPaneModelImport(String JsonFileName) throws IOException {
        VBox vBox = new VBox();
        try {
            listViewModelImport = /*new*/ ListViewModelImport.getInstance(JsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        HBox hBox = new HBox();
        TextField newModel = new TextField();
        newModel.setPromptText("Inserez le nouveau model");
        Button addModelImportBTN = new Button("Nouveau");
        hBox.getChildren().addAll(newModel, addModelImportBTN);

        addModelImportBTN.setOnAction(event -> {
            try {
                if(JsonModelImport.getInstance().addModelImport(JsonFileName, newModel.getText(), null))
                    listViewModelImport.addItem(newModel.getText());
            } catch (IOException e) {
                e.printStackTrace();
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
        });

        vBox.getChildren().addAll(listViewModelImport,  hBox);
        this.getItems().addAll(vBox);
    }

    public ListViewModelImport getListViewComponent(){
        return listViewModelImport;
    }
}
