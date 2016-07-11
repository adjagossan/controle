package com.agar.factory;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by SDEV2 on 08/07/2016.
 */
public class DaoFactoryTest {

    @Test
    public void getInstanceTest(){
        try {
            Assert.assertNotNull(DaoFactory.getInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConnectionTest(){
        try {
            Assert.assertNotNull(DaoFactory.getInstance().getConnection());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
