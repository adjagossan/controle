package com.agar.view.alert;

import javafx.beans.NamedArg;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by SDEV2 on 01/07/2016.
 */
public class ExceptionHandler extends Alert {

    public ExceptionHandler(@NamedArg("alertType") AlertType alertType, @NamedArg("contentText") String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
    }
}
