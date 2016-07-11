package com.agar.dao;

import com.agar.factory.DaoFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by SDEV2 on 08/07/2016.
 */
public interface Dao<T> {


    default Connection getConnection() throws SQLException, ClassNotFoundException {
        return DaoFactory.getInstance().getConnection();
    }
}
