package com.agar.view.alert;

import javafx.beans.NamedArg;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Created by SDEV2 on 08/07/2016.
 */
public class Login extends Alert {

    public Login(@NamedArg("contentText") String contentText, ButtonType... buttons) {
        super(AlertType.NONE, contentText, ButtonType.OK, ButtonType.CANCEL);
        Label server = new Label("Serveur");
        Label user = new Label("Utilisateur");
        Label password = new Label("Mot de passe");
        Label database = new Label("Base de donnée");

        TextField serverValue = new TextField();
        TextField userValue = new TextField();
        TextField passwordValue = new TextField();
        TextField databaseValue = new TextField();

        //Button connection = new Button("Se connecter");
        //Button
        GridPane gridPane = new GridPane();

        gridPane.add(server, 1, 1);
        gridPane.add(serverValue, 2, 1);

        gridPane.add(user, 1, 2);
        gridPane.add(userValue, 2, 2);

        gridPane.add(password, 1, 3);
        gridPane.add(passwordValue, 2, 3);

        gridPane.add(database, 1, 4);
        gridPane.add(databaseValue, 2, 4);
    }
}
