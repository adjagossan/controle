package com.agar.view;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.factory.DaoFactory;
import com.agar.view.alert.ExceptionHandler;
import com.agar.view.alert.Login;
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
        //TextField newModel = new TextField();
        //newModel.setPromptText("Inserez le nouveau model");
        Button addModelImportBTN = new Button("Ajouter un nouveau Modèle");
        hBox.getChildren().addAll(/*newModel, */addModelImportBTN);

        addModelImportBTN.setOnAction(event -> {
            callWindowLogin();
            /*try {
                if(JsonModelImport.getInstance().addModelImport(JsonFileName, newModel.getText(), null))
                    listViewModelImport.addItem(newModel.getText());
            } catch (IOException e) {
                e.printStackTrace();
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }*/
        });

        vBox.getChildren().addAll(listViewModelImport,  hBox);
        this.getItems().addAll(vBox);
    }

    public ListViewModelImport getListViewComponent(){
        return listViewModelImport;
    }

    public void callWindowLogin(){
        Login login = new Login(null);
        Optional<ButtonType> result = login.showAndWait();

        if(result.get() == ButtonType.OK){
            DaoFactory dao = null;
            Connection connection;
            try {
                dao = DaoFactory.getInstance();
                if(dao != null){
                    connection = dao.getConnection();
                    if(connection != null){
                        login.config();
                        Stage stage = new Stage(StageStyle.UTILITY);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        BorderPane borderPane = new BorderPane();
                        borderPane.setCenter(new DualTreeView());
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

    @Override
    public void update(Subject subject) throws IOException {
        this.listViewModelImport.addItem((String) subject.getValue());
    }
}
