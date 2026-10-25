package com.lostfound;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Provides a JDBC connection to the MySQL database. */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/lostfound";
    private static final String USER = "root";
    private static final String PASSWORD = "PASSWORD";   // change to your MySQL password

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
