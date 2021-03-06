package com.agar.dao;

import java.sql.*;
import java.util.*;
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
    public TreeMap<String, Set<String>>/*List<String>*/ getTables() throws SQLException
    {
        //List<String> list = new ArrayList<>();
        TreeMap<String, Set<String>> map = new TreeMap<>();
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(connection.getCatalog(), null, "%"/*null*/, TABLE_TYPES);
        while(resultSet.next()){
            //list.add(resultSet.getString(TABLE_NAME));
            map.put(resultSet.getString(TABLE_NAME), new HashSet<>());
        }
        connection.close();
        map.forEach((s, strings) -> System.out.println(s+" -> "+String.join(",",strings)));
        //Collections.sort(list);
        //list.forEach(System.out::println);
        return map/*list*/;
    }

    /**
     * TODO A REVOIR
     * @return
     * @throws SQLException
     */
    public TreeMap<String, Set<String>> getTablesWithAssociatedColumns() throws SQLException {
        TreeMap<String, Set<String>> map = new TreeMap<>();
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(connection.getCatalog(), null, "%"/*null*/, TABLE_TYPES);
        resultSet.setFetchSize(10);
        while(resultSet.next()){
            map.put(resultSet.getString(TABLE_NAME), new HashSet<>());

            ResultSet fields = dmd.getColumns(connection.getCatalog(), null, resultSet.getString(TABLE_NAME), "%"/*null*/);

            while(fields.next()){
                map.get(resultSet.getString(TABLE_NAME)).add(fields.getString("COLUMN_NAME"));
            }
        }
        connection.close();
        map.forEach((s, strings) -> System.out.println(s+" -> "+String.join(",",strings)));
        return map;
    }


    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public TreeMap<String, Set<String>> getColumns(String tableName) throws SQLException {
        TreeMap<String, Set<String>> map = new TreeMap<>();
        map.put(tableName, new HashSet<>());
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet fields = dmd.getColumns(connection.getCatalog(), null, tableName, "%"/*null*/);
        while(fields.next())
            map.get(tableName).add(fields.getString("COLUMN_NAME"));
        connection.close();
        map.forEach((s, strings) -> System.out.println(s+" -> "+String.join(",",strings)));
        return map;
    }
}
