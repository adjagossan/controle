package com.agar.view;

import com.agar.dao.DatabaseTableDao;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by SDEV2 on 12/07/2016.
 */
public class DualTreeView extends GridPane {
    private DatabaseTableDao dao;

    public DualTreeView() throws SQLException, ClassNotFoundException {
        this.setPadding(new Insets(5));
        this.setHgap(10);
        this.setVgap(10);

        this.dao = new DatabaseTableDao();

        ColumnConstraints leftConstraint = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        leftConstraint.setHgrow(Priority.ALWAYS);
        ColumnConstraints middleConstraint = new ColumnConstraints(50);
        ColumnConstraints rightConstraint = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        rightConstraint.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(leftConstraint, middleConstraint, rightConstraint);
        init();
    }

    public void init() throws SQLException, ClassNotFoundException {
        Label candidateTablesLabel = new Label("Tables candidates");
        GridPane.setHalignment(candidateTablesLabel, HPos.CENTER);
        this.add(candidateTablesLabel, 0, 0);

        Label selectedTablesLabel = new Label("Tables sélectionnées");
        GridPane.setHalignment(selectedTablesLabel, HPos.CENTER);
        this.add(selectedTablesLabel, 2, 0);

        DBTablesTree leftDBTablesTree = new DBTablesTree(dao.getTablesWithAssociatedColumns());

        this.add(leftDBTablesTree, 0, 1);

        Button sendRightButton = new Button("->");
        Button sendLeftButton = new Button("<-");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        this.add(vbox, 1, 1);

        TreeMap<String, Set<String>> emptyTreeMap = new TreeMap<>();
        DBTablesTree rightDBTablesTree = new DBTablesTree(emptyTreeMap);
        this.add(rightDBTablesTree, 2, 1);

        sendRightButton.setOnAction(event -> {
            leftDBTablesTree.remove(leftDBTablesTree.getSelectedObservableMap());
            rightDBTablesTree.setItems(leftDBTablesTree.getSelectedObservableMap());
            leftDBTablesTree.getSelectedObservableMap().clear();
        });

        sendLeftButton.setOnAction(event -> {
            rightDBTablesTree.remove(rightDBTablesTree.getSelectedObservableMap());
            leftDBTablesTree.setItems(rightDBTablesTree.getSelectedObservableMap());
            rightDBTablesTree.getSelectedObservableMap().clear();
        });

        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Annuler");

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(okButton, cancelButton);
        this.add(hbox, 1, 3);
    }
}
