package com.example.se330_pharmacy.Models;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.*;
import javax.swing.*;
public class ConnectDB {
    private static ConnectDB instance;
    public Connection databaseLink;
    public ConnectDB()
    {
        databaseLink = getConnection();
    }


    public static ConnectDB getInstance() {
        if (instance == null) {
            synchronized (ConnectDB.class) {
                if (instance == null) {
                    instance = new ConnectDB();
                }
            }
        }
        return instance;
    }

    public static Connection getConnection() {
        /*String databaseName ="PharmacyNeon";
        String databaseUser ="postgres";
        String databasePassword="phuan03042004";
        String urlPostgres="jdbc:postgresql://localhost:5432/"+databaseName;*/
        String urlNeon_DB = "jdbc:postgresql://ep-jolly-block-a52e1a3c.us-east-2.aws.neon.tech/PharmacyDB2705?user=PharmacyDB_owner&password=xKkZe1NrSpq7&sslmode=require";
        Connection connection = null;
        try {
            if (isInternetAvailable()) {
                Class.forName("org.postgresql.Driver");
                /*connection= DriverManager.getConnection(urlPostgres,databaseUser,databasePassword);*/
                connection = DriverManager.getConnection(urlNeon_DB);
                if (connection != null) System.out.println("Connection Established");
                else System.out.println("Connection Failed");
            }
            else {
                JOptionPane.showMessageDialog(null,"Không có kết nối internet");
            }
        }
        catch (Exception e ){
            e.printStackTrace();
            e.getCause();
        }
        return connection;
    }

    private static boolean isInternetAvailable() {
        boolean reachable = false;
        try {
            reachable = InetAddress.getByName("www.google.com").isReachable(500);// Kiểm tra kết nối tới Google trong 3 giây
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reachable;
    }

    public ResultSet getData(String sqlQuery) {
        try {
            PreparedStatement preparedStatement = databaseLink.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean handleData(PreparedStatement preparedStatement) throws SQLException {
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } finally {
            preparedStatement.close();
        }
    }

    public int getId(PreparedStatement preparedStatement) throws SQLException {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1); // Assuming the ID is in the first column
            }
            return -1; // Return -1 if no ID found
        } finally {
            preparedStatement.close();
        }
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        Statement statement = databaseLink.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }


    public PreparedStatement getPreparedStatement(String sqlQuery) throws SQLException {
        // Check if the connection is null or closed
        if (databaseLink == null || databaseLink.isClosed()) {
            // Handle the case when connection is not available or closed
            // Maybe throw an exception or handle it according to your application logic
            throw new SQLException("Connection is not available or closed.");
        }

        PreparedStatement preparedStatement = null;
        try {
            // Create the PreparedStatement object using the connection and SQL query
            preparedStatement = databaseLink.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            // Handle SQL exception
            // Maybe log the error or throw it further
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }

        return preparedStatement;
    }
}
