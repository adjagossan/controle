package com.agar.view;

import com.agar.factory.DaoFactory;
import com.agar.utils.Utils;
import com.agar.view.alert.ExceptionHandler;
import com.agar.view.alert.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class MainScreen extends Application {
    private DBTablesTree  dbTablesTree;

    @Override
    public void start(Stage stage)
    {
        //scenarioDualView(stage);
        scenarioTabPane(stage);
        //login();
    }

    public static void main(String[] args){
        Application.launch(args);
    }

    public void scenarioTabPane(Stage stage){
        TabPane tabPane = new TabPane();

        Tab tabImport = new Tab("Import");
        Tab tabControl = new Tab("Contrôle");

        tabPane.getTabs().addAll(tabImport, tabControl);

        SplitPaneModelImport splitPaneModelImport = null;
        try {
            splitPaneModelImport = /*new*/ SplitPaneModelImport.getInstance(Utils.modelImportJsonFileName);
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
            Scene scene = new Scene(tabPane, 500, 500);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void scenarioDualView(Stage stage){
        Utils.configCommand();
        BorderPane root = new BorderPane();
        try {
            root.setCenter(new DualTreeView(null));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,1300, 800, Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }

    public void login(){
        Login login = new Login(null);
        Optional<ButtonType> result = login.showAndWait();

        if(result.get() == ButtonType.OK){
            DaoFactory dao = null;
            Connection connection;
            try {
                login.config();
                dao = DaoFactory.getInstance();
                if(dao != null){
                    connection = dao.getConnection();
                    if(connection != null){
                        Stage stage = new Stage(StageStyle.UTILITY);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        BorderPane borderPane = new BorderPane();
                        borderPane.setCenter(new DualTreeView(null));
                        stage.setScene(new Scene(borderPane, 1300, 800, Color.WHITE));
                        stage.setTitle("");
                        stage.setResizable(false);
                        stage.show();
                    }
                }
            } catch (ClassNotFoundException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            } catch (SQLException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
        }
    }
}
