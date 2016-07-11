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
    private final String TABLE_NAME = "TABLE_NAME";
    private final String[] TABLE_TYPES = {"TABLE"};

    public DatabaseTableDao() throws SQLException, ClassNotFoundException {
        this.connection = getConnection();
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public List<String> getTables() throws SQLException
    {
        List<String> list = new ArrayList<>();

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(connection.getCatalog(), null, null, TABLE_TYPES);

        while(resultSet.next()){
            list.add(resultSet.getString(TABLE_NAME));
            System.out.println(resultSet.getString(TABLE_NAME));
        }

        connection.close();
        return list;
    }

    /**
     * TODO A REVOIR
     * @return
     * @throws SQLException
     */
    public Map<String, List<String>> getTablesWithAssociatedColumns() throws SQLException {
        Map<String, List<String>> map = new HashMap<>();

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(connection.getCatalog(), null, null, TABLE_TYPES);

        while(resultSet.next()) {
            map.put(resultSet.getString(TABLE_NAME), new ArrayList<>());
            ResultSet fields = dmd.getColumns(null, null, resultSet.getString(TABLE_NAME), null);
            while(fields.next()){
                map.get(resultSet.getString(TABLE_NAME)).add(fields.getString("COLUMN_NAME"));
                map.forEach((s, strings) -> System.out.println(s+" "+strings.toArray().toString()));
            }
        }
        connection.close();
        return map;
    }


    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public Map<String, List<String>> getColumns(String tableName) throws SQLException {
        Map<String, List<String>> map = new HashMap<>();
        map.put(tableName, new ArrayList<>());

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet fields = dmd.getColumns(null, null, tableName, null);
        while(fields.next())
            map.get(tableName).add(fields.getString("COLUMN_NAME"));

        connection.close();
        return map;
    }
}
