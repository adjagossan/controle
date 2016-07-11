package com.agar.dao;

import org.junit.Assert;
import org.junit.Test;
import java.sql.SQLException;

/**
 * Created by SDEV2 on 11/07/2016.
 */
public class DatabaseTableDaoTest {

    @Test
    public void getTablesTest(){
        try {
            Assert.assertNotNull(new DatabaseTableDao().getTables());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getColumnsTest(){
        String tableName = "dossier_csis";
        try {
            Assert.assertNotNull(new DatabaseTableDao().getColumns(tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
