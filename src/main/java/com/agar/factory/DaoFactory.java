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
    private static String databaseName = "ReportServerTempDB";//AcaciasCucq
    private static String hostName = "SRVTEST";
    private static String URL = "jdbc:mysql://localhost:3306/sakila?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&profileSQL=false&useSSL=false";//"jdbc:sqlserver://"+hostName+";databaseName="+databaseName;
    private static String DRIVER =  "com.mysql.cj.jdbc.Driver"; //"com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String USERNAME = "root";//"sa";
    private static String PASSWORD = "root";//"sqladmin";
    private static final int minConnectionsPerPartition = 5;
    private static final int maxConnectionsPerPartition = 10;
    private static final int partitionCount = 2;

    private DaoFactory(BoneCP connectionPool){
        this.connectionPool = connectionPool;
    }

    public static DaoFactory getInstance() throws ClassNotFoundException, SQLException{
        BoneCP connectionPool;

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        try{
            BoneCPConfig config = new BoneCPConfig();
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
        return instance;
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

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
