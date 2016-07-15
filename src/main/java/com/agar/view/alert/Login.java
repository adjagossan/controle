package com.agar.view.alert;

import com.agar.factory.DaoFactory;
import javafx.beans.NamedArg;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Created by SDEV2 on 08/07/2016.
 */
public class Login extends Alert {

    private TextField serverValue = new TextField();
    private TextField userValue = new TextField();
    private TextField passwordValue = new TextField();
    private TextField databaseValue = new TextField();

    public Login(@NamedArg("contentText") String contentText/*, ButtonType... buttons*/) {
        super(AlertType.NONE, contentText, ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Label server = new Label("Serveur");
        Label user = new Label("Utilisateur");
        Label password = new Label("Mot de passe");
        Label database = new Label("Base de donnée");

        serverValue = new TextField();
        userValue = new TextField();
        passwordValue = new TextField();
        databaseValue = new TextField();

        GridPane gridPane = new GridPane();

        gridPane.add(server, 1, 1);
        gridPane.add(serverValue, 2, 1);

        gridPane.add(user, 1, 2);
        gridPane.add(userValue, 2, 2);

        gridPane.add(password, 1, 3);
        gridPane.add(passwordValue, 2, 3);

        gridPane.add(database, 1, 4);
        gridPane.add(databaseValue, 2, 4);

        gridPane.setVgap(5);
        gridPane.setHgap(5);
        this.getDialogPane().setContent(gridPane);
    }

    public void config(){
        DaoFactory.setDatabaseName(databaseValue.getText().toString());
        DaoFactory.setHostName(serverValue.getText().toString());
        DaoFactory.setUSERNAME(userValue.getText().toString());
        DaoFactory.setPASSWORD(passwordValue.getText().toString());
    }
}
