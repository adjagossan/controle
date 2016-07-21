package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.data.JsonModelImport;
import com.agar.factory.DaoFactory;
import com.agar.view.alert.ExceptionHandler;
import com.agar.view.alert.Login;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
public class SplitPaneModelImport extends SplitPane implements Subscriber
{
    private ListViewModelImport listViewModelImport;
    private static SplitPaneModelImport splitPaneModelImport/* = new SplitPaneModelImport()*/;
    //private Connection connection;
    //private boolean isConnected = false;
    private static BooleanProperty isConnected = new SimpleBooleanProperty(false);

    public static SplitPaneModelImport getInstance(String JsonFileName) throws IOException {
        if(splitPaneModelImport == null)
            splitPaneModelImport = new SplitPaneModelImport(JsonFileName);
        return splitPaneModelImport;
    }

    public static SplitPaneModelImport getInstance(){
        return splitPaneModelImport;
    }

    private SplitPaneModelImport(String JsonFileName) throws IOException {
        VBox vBox = new VBox();
        try {
            listViewModelImport = /*new*/ ListViewModelImport.getInstance(JsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(100);
        hBox.setPadding(new Insets(25));
        Button addModelImportBTN = new Button("Ajouter un nouveau Modèle");
        Button signOutBTN = new Button("Se déconnecter");
        signOutBTN.setOnAction(event -> {
            /*this.*/isConnected.set(false);
            try {
                DaoFactory.getInstance().shutDown();
            } catch (ClassNotFoundException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            } catch (SQLException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
        });
        signOutBTN.visibleProperty().bind(/*this.*/isConnected);

        hBox.getChildren().addAll(addModelImportBTN, signOutBTN);

        addModelImportBTN.setOnAction(event -> {
            if(!/*this.*/isConnected.get())
                callWindowLogin(null);
            else
                showWindow(null);
        });

        vBox.getChildren().addAll(listViewModelImport,  hBox);
        this.getItems().addAll(vBox);
    }

    public static boolean isConnected() {
        return isConnected.get();
    }

    public static boolean isConnectedProperty() {
        return isConnected.get();
    }

    public ListViewModelImport getListViewComponent(){
        return listViewModelImport;
    }

    public static void callWindowLogin(String tableName){
        Login login = new Login(null);
        Optional<ButtonType> result = login.showAndWait();

        if(result.get() == ButtonType.OK){
            DaoFactory dao = null;
            Connection connection = null;
            try {
                login.config();
                dao = DaoFactory.getInstance();
                if(dao != null){
                    /*this.*/connection = dao.getConnection();
                    if(/*this.*/connection != null){
                        /*this.*/isConnected.set(true);
                        /*this.*/connection.close();
                        showWindow(tableName);
                    }
                }
            } catch (ClassNotFoundException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            } catch (SQLException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
        }
    }

    public static void showWindow(String tableName){
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        BorderPane borderPane = new BorderPane();
        try {
            borderPane.setCenter(new DualTreeView(tableName));
        } catch (SQLException e) {
            new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
        } catch (ClassNotFoundException e) {
            new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
        }
        stage.setScene(new Scene(borderPane, 1300, 800, Color.WHITE));
        stage.setTitle("");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void update(Subject subject) throws IOException {
        this.listViewModelImport.addItem((JsonModelImport.Info/*String*/) subject.getValue());
    }
}
