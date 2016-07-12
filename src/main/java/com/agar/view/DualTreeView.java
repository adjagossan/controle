package com.agar.view;

import com.agar.dao.DatabaseTableDao;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
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

        TreeMap<String, List<String>> emptyTreeMap = new TreeMap<>();
        DBTablesTree rightDBTablesTree = new DBTablesTree(emptyTreeMap);
        this.add(rightDBTablesTree, 2, 1);
    }
}
