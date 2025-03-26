package com.degreeflow.service;

import com.degreeflow.database.AzurePostgresConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseService {

    public void testConnection() {
        try (Connection connection = AzurePostgresConnection.connectToPostgres()) {
            if (connection != null) {
                System.out.println("Successfully connected to PostgreSQL with Managed Identity!");
            } else {
                System.out.println("Failed to connect to PostgreSQL!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
