package com.agar.view;

import com.agar.dao.DatabaseTableDao;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DBTablesTreeStackPane extends StackPane {
    private DatabaseTableDao dao;
    private Map<String, List<String>> map;

    public DBTablesTreeStackPane() throws SQLException, ClassNotFoundException {
        DatabaseTableDao dao = new DatabaseTableDao();
        Map<String, List<String>> map = dao.getTablesWithAssociatedColumns();
        init();
    }

    public void init(){
        for(Map.Entry<String, List<String>> entry : this.map.entrySet())
            this.getChildren().add(new DBTablesTree((Map<String, List<String>>) entry));
    }
}
