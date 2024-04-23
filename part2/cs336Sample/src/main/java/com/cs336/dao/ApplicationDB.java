package com.cs336.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationDB {
    
    public ApplicationDB() {
        // Constructor body can be empty if nothing to initialize
    }

    public Connection getConnection() {
        String connectionUrl = "jdbc:mysql://localhost:3306/FinalProject";
        Connection connection = null;

        try {
            // This assumes you have the MySQL connector JAR in your classpath
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionUrl, "root", "root-password");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Main method could be used for testing
    public static void main(String[] args) {
        ApplicationDB db = new ApplicationDB();
        Connection connection = db.getConnection();
        
        System.out.println(connection);
        db.closeConnection(connection);
    }
}