package com.agar.view;

import com.agar.utils.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class MainScreen extends Application {
    private DBTablesTree  dbTablesTree;

    @Override
    public void start(Stage stage)
    {
        /*TabPane tabPane = new TabPane();

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
            }
            Scene scene = new Scene(tabPane, 500, 500);
            stage.setScene(scene);
            stage.show();
        }*/
        /*VBox vbox = new VBox();
        try {
            dbTablesTree = new DBTablesTree(new DatabaseTableDao().getTablesWithAssociatedColumns());
            vbox.getChildren().add(dbTablesTree);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(vbox, 500, 500);
        stage.setScene(scene);
        stage.show();*/
        scenarioDualView(stage);
    }

    public static void main(String[] args){
        Application.launch(args);
    }

    public void scenarioDualView(Stage stage){
        Utils.configCommand();
        BorderPane root = new BorderPane();
        try {
            root.setCenter(new DualTreeView());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,1300, 800, Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }
}
