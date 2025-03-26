package com.degreeflow.database;

import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.core.credential.TokenRequestContext;
import org.springframework.beans.factory.annotation.Value;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AzurePostgresConnection {
    @Value("${spring.datasource.url}")
    private static String DB_URL;

    @Value("${spring.datasource.username}")
    private static String DB_USERNAME;  // Managed Identity username (can be a dummy string like "postgres")

    public static Connection connectToPostgres() {
        try {
            // Create ManagedIdentityCredential
            ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder()
                    .build();

            // Fetch the access token using Managed Identity
            String accessToken = credential.getToken(
                    new TokenRequestContext().addScopes("https://ossrdbms-aad.database.windows.net/.default")
            ).block().getToken();

            // Set the connection properties for JDBC
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, accessToken);
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
