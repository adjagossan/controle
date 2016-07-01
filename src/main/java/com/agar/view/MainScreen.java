package com.agar.view;

import com.agar.utils.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class MainScreen extends Application {

    @Override
    public void start(Stage stage) throws Exception
    {
        TabPane tabPane = new TabPane();

        Tab tabImport = new Tab("Import");
        Tab tabControl = new Tab("Contrôle");

        tabPane.getTabs().addAll(tabImport, tabControl);

        SplitPaneModelImport splitPaneModelImport = new SplitPaneModelImport(Utils.modelImportJsonFileName);
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
