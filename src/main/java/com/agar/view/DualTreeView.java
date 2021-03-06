package com.agar.view;

import com.agar.Command;
import com.agar.Invoker;
import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.dao.DatabaseTableDao;
import com.agar.data.JsonMapping;
import com.agar.data.JsonModelImport;
import com.agar.factory.DaoFactory;
import com.agar.model.Mapping;
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
    private /*String*/JsonModelImport.Info selectedModel;
    private DBTablesTree rightDBTablesTree;

    public DualTreeView(String tableName) throws SQLException, ClassNotFoundException {
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
        try {
            init(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String tableName) throws SQLException, ClassNotFoundException, IOException {
        Label candidateTablesLabel = new Label("Tables candidates");
        GridPane.setHalignment(candidateTablesLabel, HPos.CENTER);
        this.add(candidateTablesLabel, 0, 0);

        Label selectedTablesLabel = new Label("Tables sélectionnées");
        GridPane.setHalignment(selectedTablesLabel, HPos.CENTER);
        this.add(selectedTablesLabel, 2, 0);

        Label renameLabel = new Label("Renommer Table/Champ");
        GridPane.setHalignment(renameLabel, HPos.CENTER);
        this.add(renameLabel, 3, 0);

        DBTablesTree leftDBTablesTree;// = new DBTablesTree(dao.getTablesWithAssociatedColumns());
        if(tableName == null)
            leftDBTablesTree = new DBTablesTree(dao.getTablesWithAssociatedColumns());
        else
            leftDBTablesTree = new DBTablesTree(dao.getColumns(tableName));

        this.add(leftDBTablesTree, 0, 1);

        Button sendRightButton = new Button("->");
        Button sendLeftButton = new Button("<-");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        this.add(vbox, 1, 1);

        TreeMap<String, Set<String>> emptyTreeMap = new TreeMap<>();
        /*DBTablesTree*/ rightDBTablesTree = new DBTablesTree(emptyTreeMap);
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
                for(DualTreeView.Info dualTreeViewInfo : nestedGridPane.infos){
                    if(dualTreeViewInfo.getMap().containsKey(s) && dualTreeViewInfo.getType() == DBTablesTree.Type.TABLE){
                        nestedGridPane.infos.remove(dualTreeViewInfo);
                        break;
                    }
                }
                strings.forEach(s1 -> {
                    for(DualTreeView.Info dualTreeViewInfo : nestedGridPane.infos){
                        if(dualTreeViewInfo.getMap().containsKey(s1) && dualTreeViewInfo.getType() == DBTablesTree.Type.FIELD){
                            nestedGridPane.infos.remove(dualTreeViewInfo);
                            break;
                        }
                    }
                });
               /* if(nestedGridPane.map.containsKey(s))
                    nestedGridPane.map.remove(s);
                strings.forEach(s1 -> {
                    if(nestedGridPane.map.containsKey(s1))
                        nestedGridPane.map.remove(s1);
                });*/
            });
            rightDBTablesTree.getSelectedObservableMap().clear();
            nestedGridPane.clear();
        });

        Button okButton = new Button("Enregistrer");
        okButton.setPadding(new Insets(5, 50, 5, 50));
        okButton.setOnAction(event -> {
            /**********Mardi 19 Juillet 2016 14h43*************/
            Map<String, String> map = null; //= nestedGridPane.getMap();
            List<DualTreeView.Info> infos = nestedGridPane.getInfos();
            String databaseName = DaoFactory.getDatabaseName();
            Map<String, String> table = new HashMap<>();
            Map<String, String> fields = new HashMap<>();
            //Mapping mapping = new Mapping();
            String jsonFileName = "mapping.json";
            JsonMapping jsonMapping = JsonMapping.getInstance(jsonFileName);
            /**********************************************/
            rightDBTablesTree.getItems().forEach((s, strings) -> {
                /**********Mardi 19 Juillet 2016 14h50*************/
                table.clear();
                fields.clear();
                boolean foundTable = false;
                for(DualTreeView.Info dualTreeViewInfo : nestedGridPane.infos){
                    if(dualTreeViewInfo.getMap().containsKey(s) && dualTreeViewInfo.getType() == DBTablesTree.Type.TABLE){
                        table.put(s, dualTreeViewInfo.getMap().get(s));
                        foundTable = true;
                        break;
                    }
                }
                if(!foundTable)
                    table.put(s, /*""*/null);
                /**********************************************/
                strings.forEach(s1 -> {
                    try {
                        JsonModelImport.getInstance()
                                .addModelImport(Utils.modelImportJsonFileName, s, s1);
                        /**********Mardi 19 Juillet 2016 14h50*************/
                        for(DualTreeView.Info dualTreeViewInfo : nestedGridPane.infos){
                            if(dualTreeViewInfo.getMap().containsKey(s1) && dualTreeViewInfo.getType() == DBTablesTree.Type.FIELD && dualTreeViewInfo.getParent().contentEquals(s)){
                                fields.put(s1, dualTreeViewInfo.getMap().get(s1));
                                break;
                            }
                        }

                        /**********************************************/
                    } catch (IOException e) {
                        e.printStackTrace();
                        new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
                    }
                });
                /**********Mardi 19 Juillet 2016 15h00*************/
                Mapping.Component component =  /*mapping.new Component(table, fields);*/ new Mapping.Component(table, fields);
                try {
                    jsonMapping.addMapping(databaseName, component, true);
                } catch (IOException e) {
                    new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
                }
                /***************************************************/
                this.setValue(new JsonModelImport.Info(databaseName, s)/*s*/);
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
        this.selectedModel = (JsonModelImport.Info/*String*/)object;
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
        //Map<String, String> map = new HashMap<>();
        String oldValue, newValue;
        List<Mapping> mappings = new ArrayList<>();
        List<DualTreeView.Info> infos = new ArrayList<>();

        NestedGridPane() throws IOException {
            this.oldValueTextField = new TextField();
            this.oldValueTextField.setEditable(false);
            this.newValueTextField = new TextField();
            getMappings();
            this.saveButton = new Button("Enregistrer");

            this.saveButton.setOnAction(event -> {
                oldValue = this.oldValueTextField.getText().toString();
                newValue = this.newValueTextField.getText().toString();
                DBTablesTree.Info dbTablesTreeInfo = (DBTablesTree.Info)this.subject.getValue();
                boolean found = false;
                for(DualTreeView.Info info : infos){
                    if(info.getType() == dbTablesTreeInfo.getType()){
                        if(info.getMap().containsKey(dbTablesTreeInfo.getValue())){
                            info.getMap().replace(oldValue, newValue);
                            found = true;
                            break;
                        }
                    }
                }
                if(!found)
                    infos.add(new DualTreeView.Info(oldValue, newValue, dbTablesTreeInfo.getType(), dbTablesTreeInfo.getParent()));
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
            DBTablesTree.Info value = (DBTablesTree.Info) subject.getValue();
            this.oldValueTextField.setText(value.getValue());
            boolean found = false;

            for(Info info : infos){
                if(info.getType() == value.getType()){
                    if(info.getMap().containsKey(value.getValue())){
                        this.newValueTextField.setText(info.getMap().get(value.getValue()));
                        found = true;
                        break;
                    }
                }
            }
            if(!found)
                this.newValueTextField.clear();
        }

        public List<Mapping> getMappings() {
            try {
                this.mappings = JsonMapping.getInstance(Utils.mappingJsonFileName).getMappings();
                for(Mapping mapping : this.mappings){
                    if(mapping.getDatabase().contentEquals(DaoFactory.getDatabaseName())){
                        for(Mapping.Component compo : mapping.getComponents()){
                            Map<String, String> table = compo.getTable();
                            String oldValue = table.keySet().stream().findFirst().get();
                            String newValue = table.get(oldValue);
                            DualTreeView.Info info = new Info(oldValue, newValue, DBTablesTree.Type.TABLE, null);
                            infos.add(info);

                            Map<String, String> fields = compo.getFields();
                            fields.forEach((s, s2) -> {
                                DualTreeView.Info info1 = new Info(s, s2, DBTablesTree.Type.FIELD, oldValue);
                                infos.add(info1);
                            });
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
            return mappings;
        }

        public void setMappings(List<Mapping> mappings) {
            this.mappings = mappings;
        }

        public List<Info> getInfos() {
            return infos;
        }

        public void setInfos(List<Info> infos) {
            this.infos = infos;
        }
    }

    static class Info{
        Map<String, String> map = new HashMap<>();
        DBTablesTree.Type type;
        String parent;

        Info(){}

        Info(String oldValue, String newValue, DBTablesTree.Type type){
            Info.this.map.put(oldValue, newValue);
            Info.this.type = type;
        }

        Info(String oldValue, String newValue, DBTablesTree.Type type, String parent){
            Info.this.map.put(oldValue, newValue);
            Info.this.type = type;
            Info.this.parent = parent;
        }

        public DBTablesTree.Type getType() {
            return type;
        }

        public void setType(DBTablesTree.Type type) {
            this.type = type;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
    }
}
