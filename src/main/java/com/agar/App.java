package com.agar;

import com.agar.model.ModelImport;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args )
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("A");
        rootItem.setExpanded(true);

        TreeView tree = new TreeView(rootItem);
        tree.setEditable(true);

        tree.setCellFactory(CheckBoxTreeCell.forTreeView());

        CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>("a");
        rootItem.getChildren().add(checkBoxTreeItem);

        checkBoxTreeItem = new CheckBoxTreeItem<>("b");
        rootItem.getChildren().add(checkBoxTreeItem);

        tree.setRoot(rootItem);

        StackPane root = new StackPane();
        root.getChildren().add(tree);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
