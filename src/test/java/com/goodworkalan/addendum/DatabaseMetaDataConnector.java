package com.goodworkalan.addendum;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.goodworkalan.addendum.connector.Connector;

public class DatabaseMetaDataConnector implements Connector {
    private final String databaseName;
    
    public DatabaseMetaDataConnector(String databaseName) {
        this.databaseName = databaseName;
    }

    public Connection open() {
        try {
            Connection connection = mock(Connection.class);
            DatabaseMetaData meta = mock(DatabaseMetaData.class);
            when(meta.getDatabaseProductName()).thenReturn(databaseName);
            when(connection.getMetaData()).thenReturn(meta);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void close(Connection connection) {
    }
}
