package com.agar.view;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



    public class Test extends Application {
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
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


            CheckBoxTreeItem<String> rootItem_ = new CheckBoxTreeItem<>("B");
            rootItem_.setExpanded(true);

            TreeView tree_ = new TreeView(rootItem_);
            tree_.setEditable(true);

            tree_.setCellFactory(CheckBoxTreeCell.forTreeView());

            CheckBoxTreeItem<String> checkBoxTreeItem_ = new CheckBoxTreeItem<>("aee");
            rootItem_.getChildren().add(checkBoxTreeItem_);

            checkBoxTreeItem_ = new CheckBoxTreeItem<>("bee");
            rootItem_.getChildren().add(checkBoxTreeItem_);

            tree_.setRoot(rootItem_);


            StackPane root = new StackPane();
            VBox hbox = new VBox();
            //root.getChildren().add(tree);
            //root.getChildren().add(tree_);
            hbox.getChildren().addAll(tree, tree_);
            //root.getChildren().addAll(tree, tree_);
            primaryStage.setScene(new Scene(hbox, 300, 250));
            primaryStage.show();


        }
    }
