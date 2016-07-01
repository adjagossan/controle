package com.agar.view;

import com.agar.utils.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class MainScreen extends Application {

    @Override
    public void start(Stage stage)
    {
        TabPane tabPane = new TabPane();

        Tab tabImport = new Tab("Import");
        Tab tabControl = new Tab("Contrôle");

        tabPane.getTabs().addAll(tabImport, tabControl);

        SplitPaneModelImport splitPaneModelImport = null;
        try {
            splitPaneModelImport = new SplitPaneModelImport(Utils.modelImportJsonFileName);
        } catch (IOException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        tabImport.setContent(splitPaneModelImport);
        TableViewControl tableViewControl = new TableViewControl(Utils.modelImportJsonFileName,Utils.constraintJsonFileName, (String)splitPaneModelImport.getListViewComponent().getValue()/*"patient"*/);
        tabControl.setContent(tableViewControl);
        splitPaneModelImport.getListViewComponent().register(tableViewControl);

        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
