package com.agar.view;

import com.agar.dao.DatabaseTableDao;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTreeStackPane extends StackPane {
    /*private DatabaseTableDao dao;
    private TreeMap<String, List<String>> map;

    public DBTablesTreeStackPane() throws SQLException, ClassNotFoundException {
        this.dao = new DatabaseTableDao();
        this.map = dao.getTables/*WithAssociatedColumns();
        System.out.println("-> "+this.map.size());
        init();
    }

    public void init(){

        this.map.forEach((s, strings) -> {this.getChildren().add(new DBTablesTree(s, strings));System.out.println("Node-> "+this.getChildren().size());});
    }*/
}
