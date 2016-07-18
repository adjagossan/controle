package com.agar.factory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Created by SDEV2 on 08/07/2016.
 */
public class DaoFactory {
    private BoneCP connectionPool;
    private static DaoFactory instance;
    private static String databaseName; //= "ReportServerTempDB";//AcaciasCucq //sakila
    private static String hostName; //= "SRVTEST";
    //private static String URL = "jdbc:mysql://"+hostName+":3306/"+databaseName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&profileSQL=false&useSSL=false";
    private static String URL = "jdbc:sqlserver://"+hostName+";databaseName="+databaseName;
    //private static String DRIVER =  "com.mysql.cj.jdbc.Driver";
    private static String DRIVER =  "com.microsoft.sqlserver.jdbc.SQLServerDriver";//"com.mysql.cj.jdbc.Driver";
    private static String USERNAME;// =/*"sa";*/"root";
    private static String PASSWORD;// = /*"sqladmin";*/"root";
    private static final int minConnectionsPerPartition = 5;
    private static final int maxConnectionsPerPartition = 10;
    private static final int partitionCount = 2;

    private DaoFactory(/*BoneCP connectionPool*/) throws SQLException, ClassNotFoundException {
        //this.connectionPool = connectionPool;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        try{
            BoneCPConfig config = new BoneCPConfig();
            System.out.println("url :"+URL);
            config.setJdbcUrl(URL);
            config.setUsername(USERNAME);
            config.setPassword(PASSWORD);
            config.setMinConnectionsPerPartition(minConnectionsPerPartition);
            config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
            config.setPartitionCount(partitionCount);
            this.connectionPool = new BoneCP(config);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static DaoFactory getInstance() throws ClassNotFoundException, SQLException{
        if(instance == null)
            instance = new DaoFactory();
        return instance;
        /*BoneCP connectionPool;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        try{
            BoneCPConfig config = new BoneCPConfig();
            System.out.println("url :"+URL);
            config.setJdbcUrl(URL);
            config.setUsername(USERNAME);
            config.setPassword(PASSWORD);
            config.setMinConnectionsPerPartition(minConnectionsPerPartition);
            config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
            config.setPartitionCount(partitionCount);
            connectionPool = new BoneCP(config);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        DaoFactory instance = new DaoFactory(connectionPool);
        return instance;*/
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static void setDatabaseName(String databaseName) {
        DaoFactory.databaseName = databaseName;
    }

    public static void setHostName(String hostName) {
        DaoFactory.hostName = hostName;
    }

    public static void setUSERNAME(String USERNAME) {
        DaoFactory.USERNAME = USERNAME;
    }

    public static void setPASSWORD(String PASSWORD) {
        DaoFactory.PASSWORD = PASSWORD;
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String hostName, String databaseName) {
        //URL = "jdbc:mysql://"+hostName+":3306/"+databaseName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&profileSQL=false&useSSL=false";
        URL = "jdbc:sqlserver://"+hostName+";databaseName="+databaseName;
        System.out.println("-> url :"+URL);
    }

    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection();
    }

    public void shutDown(){
        this.connectionPool.shutdown();
    }
}
