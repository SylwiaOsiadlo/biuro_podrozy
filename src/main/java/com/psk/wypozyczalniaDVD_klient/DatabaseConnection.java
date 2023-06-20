package com.psk.wypozyczalniaDVD_klient;

import java.sql.*;

public class DatabaseConnection {

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
            throw new RuntimeException(e);
        }

    }

    public void closeDbConnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
