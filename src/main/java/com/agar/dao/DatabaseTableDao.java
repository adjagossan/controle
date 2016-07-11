package com.agar.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DatabaseTableDao implements Dao {

    private Connection connection;

    public DatabaseTableDao() throws SQLException, ClassNotFoundException {
        this.connection = getConnection();
    }

    public List<String> getTables() throws SQLException
    {
        final String TABLE_NAME = "TABLE_NAME";
        final String[] TABLE_TYPES = {"TABLE"};
        List<String> list = new ArrayList<>();

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(connection.getCatalog(), null, null, TABLE_TYPES);

        while(resultSet.next()){
            list.add(resultSet.getString(TABLE_NAME));
            //System.out.println(resultSet.getString(TABLE_NAME));
        }
        connection.close();
        return list;
    }

    public Map<String, List<String>> getColumns(String tableName) throws SQLException {
        Map<String, List<String>> map = new HashMap<>();
        map.put(tableName, new ArrayList<>());

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet fields = dmd.getColumns(null, null, tableName, null);
        while(fields.next()){
            map.get(tableName).add(fields.getString("COLUMN_NAME"));
            //System.out.println("-> "+fields.getString("COLUMN_NAME"));
        }
        connection.close();
        return map;
    }
}
