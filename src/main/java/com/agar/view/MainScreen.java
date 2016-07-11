package com.agar.view;

import com.agar.utils.Utils;
import com.agar.view.alert.ExceptionHandler;
import com.agar.view.alert.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class MainScreen extends Application {
    private DBTablesTreeStackPane stackPane;
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
            tabImport.setContent(splitPaneModelImport);
            TableViewControl tableViewControl = new TableViewControl(
                    Utils.modelImportJsonFileName,
                    Utils.constraintJsonFileName,
                    (String)splitPaneModelImport.getListViewComponent().getValue());
            tabControl.setContent(tableViewControl);
            splitPaneModelImport.getListViewComponent().register(tableViewControl);
        } catch (IOException e){
            new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
        }
        finally{
            /*Login login = new Login("");
            login.showAndWait();
            try {
                stackPane = new DBTablesTreeStackPane();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/
            Scene scene = new Scene(tabPane, 500, 500);
            stage.setScene(scene);
            stage.show();
        }

    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
