package com.psk.wypozyczalniaDVD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseConnection {

    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    private static String url = "jdbc:mysql://localhost:3306/wypozyczalnia_dvd";
    private static String username = "root";
    private static String password = "";

    private Connection connection;

    public DatabaseConnection() {
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void closeDbConnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
