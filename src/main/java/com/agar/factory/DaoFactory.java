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
    private static String URL = "jdbc:sqlserver://SRVTEST;databaseName=AcaciasCucq";
    private static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String USERNAME = "sa";
    private static String PASSWORD = "sqladmin";
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

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
