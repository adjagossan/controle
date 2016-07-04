package com.agar.view.dialog;

/**
 * Created by SDEV2 on 04/07/2016.
 */
public class TextInputDialog extends javafx.scene.control.TextInputDialog {
            public TextInputDialog(String title, String context, String hint){
                    super(hint);
                    this.setTitle(title);
                    this.setContentText(context);
            }
}
