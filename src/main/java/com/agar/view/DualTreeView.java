package com.agar.view;

import com.agar.Command;
import com.agar.Invoker;
import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.dao.DatabaseTableDao;
import com.agar.data.JsonModelImport;
import com.agar.utils.Utils;
import com.agar.view.alert.ExceptionHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by SDEV2 on 12/07/2016.
 */
public class DualTreeView extends GridPane implements Subject {
    private DatabaseTableDao dao;
    private List<Subscriber> subscribers = new ArrayList<>();
    private String selectedModel;

    public DualTreeView() throws SQLException, ClassNotFoundException {
        this.setPadding(new Insets(5));
        this.setHgap(10);
        this.setVgap(10);
        this.setMaxWidth(Double.MAX_VALUE);
        this.dao = new DatabaseTableDao();

        ColumnConstraints leftConstraint = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        leftConstraint.setHgrow(Priority.ALWAYS);
        ColumnConstraints middleConstraint = new ColumnConstraints(50);
        ColumnConstraints rightConstraint = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        rightConstraint.setHgrow(Priority.ALWAYS);
        ColumnConstraints afterRightConstraint = new ColumnConstraints(250, 250, Double.MAX_VALUE);
        afterRightConstraint.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(leftConstraint, middleConstraint, rightConstraint, afterRightConstraint);
        this.register(SplitPaneModelImport.getInstance());
        init();
    }

    public void init() throws SQLException, ClassNotFoundException {
        Label candidateTablesLabel = new Label("Tables candidates");
        GridPane.setHalignment(candidateTablesLabel, HPos.CENTER);
        this.add(candidateTablesLabel, 0, 0);

        Label selectedTablesLabel = new Label("Tables sélectionnées");
        GridPane.setHalignment(selectedTablesLabel, HPos.CENTER);
        this.add(selectedTablesLabel, 2, 0);

        Label renameLabel = new Label("Renommer Table/Champ");
        GridPane.setHalignment(renameLabel, HPos.CENTER);
        this.add(renameLabel, 3, 0);

        DBTablesTree leftDBTablesTree = new DBTablesTree(dao.getTablesWithAssociatedColumns());

        this.add(leftDBTablesTree, 0, 1);

        Button sendRightButton = new Button("->");
        Button sendLeftButton = new Button("<-");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        this.add(vbox, 1, 1);

        TreeMap<String, Set<String>> emptyTreeMap = new TreeMap<>();
        DBTablesTree rightDBTablesTree = new DBTablesTree(emptyTreeMap);
        rightDBTablesTree.setStatus(DBTablesTree.Status.SELECTED);
        this.add(rightDBTablesTree, 2, 1);

        NestedGridPane nestedGridPane = new NestedGridPane();
        rightDBTablesTree.register(nestedGridPane);
        this.add(nestedGridPane, 3, 1);

        sendRightButton.setOnAction(event -> {
            leftDBTablesTree.remove(leftDBTablesTree.getSelectedObservableMap());
            rightDBTablesTree.setItems(leftDBTablesTree.getSelectedObservableMap());
            leftDBTablesTree.getSelectedObservableMap().clear();
        });

        sendLeftButton.setOnAction(event -> {
            rightDBTablesTree.remove(rightDBTablesTree.getSelectedObservableMap());
            leftDBTablesTree.setItems(rightDBTablesTree.getSelectedObservableMap());
            rightDBTablesTree.getSelectedObservableMap().forEach((s, strings) -> {
                if(nestedGridPane.map.containsKey(s))
                    nestedGridPane.map.remove(s);
                strings.forEach(s1 -> {
                    if(nestedGridPane.map.containsKey(s1))
                        nestedGridPane.map.remove(s1);
                });
            });
            rightDBTablesTree.getSelectedObservableMap().clear();
            nestedGridPane.clear();
        });

        Button okButton = new Button("Enregistrer");
        okButton.setPadding(new Insets(5, 50, 5, 50));
        okButton.setOnAction(event -> {
            rightDBTablesTree.getItems().forEach((s, strings) -> {
                strings.forEach(s1 -> {
                    try {
                        JsonModelImport.getInstance()
                                .addModelImport(Utils.modelImportJsonFileName, s, s1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
                    }
                });
                this.setValue(s);
                /*Command cmd =Invoker.getInstance().getCommand(Utils.Cmd.ADD_MODEL_IMPORT);
                if(cmd != null){
                    cmd.
                }*/
            });
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.close();
        });

        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.close();
        });

        cancelButton.setPadding(new Insets(5, 50, 5, 50));
        HBox hbox = new HBox(10);
        hbox.setSpacing(50);
        hbox.getChildren().addAll(okButton, cancelButton);
        this.add(hbox, 2, 3);
    }

    @Override
    public void register(Subscriber subscriber) {
        if(!subscribers.contains(subscriber))subscribers.add(subscriber);
    }

    @Override
    public void unregister(Subscriber subscriber) {
        if(subscribers.contains(subscriber))subscribers.remove(subscriber);
    }

    @Override
    public boolean isAttached(Subscriber subscriber) {
        return subscribers.contains(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> {
            try {
                subscriber.update(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setValue(Object object) {
        this.selectedModel = (String)object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return selectedModel;
    }

    class NestedGridPane extends GridPane implements Subscriber{
        TextField oldValueTextField;
        TextField newValueTextField ;
        Button saveButton;
        Subject subject;
        Map<String, String> map = new HashMap<>();
        String oldValue, newValue;

        NestedGridPane(){
            this.oldValueTextField = new TextField();
            this.oldValueTextField.setEditable(false);
            this.newValueTextField = new TextField();
            this.saveButton = new Button("Enregistrer");

            this.saveButton.setOnAction(event -> {
                oldValue = this.oldValueTextField.getText().toString();
                newValue = this.newValueTextField.getText().toString();
                if(this.map.containsKey(oldValue))
                    this.map.replace(oldValue, newValue);
                else
                    map.put(oldValue,newValue);
                //((DBTablesTree)this.subject).getSelectionModel().clearSelection();
                this.clear();
            });

            this.add(new Label("Sélection"), 0, 0);
            this.add(new Label("Nouvelle valeur"), 0, 1);
            this.add(oldValueTextField, 1, 0);
            this.add(newValueTextField, 1, 1);
            this.add(saveButton, 1, 2);
            this.setVgap(10);
            this.setHgap(10);
            this.setStyle("-fx-border-width: 1.0; -fx-border-color: gray;");
        }

        public void clear(){
            this.oldValueTextField.clear();
            this.newValueTextField.clear();
        }

        @Override
        public void update(Subject subject){
            this.subject = subject;
            String value = (String) subject.getValue();
            this.oldValueTextField.setText(value);
            if(map.containsKey(value))
                this.newValueTextField.setText(map.get(value));
            else
                this.newValueTextField.clear();
        }
    }
}
