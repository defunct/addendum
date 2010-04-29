package com.goodworkalan.addendum.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.goodworkalan.addendum.Connector;

public class MockConnector implements Connector {
    public Connection open() {
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        Connection connection = mock(Connection.class);
        try {
            when(connection.getMetaData()).thenReturn(meta);
        } catch (SQLException e) {
        }
        return connection;
    }
    
    
    public void close(Connection connection) {
    }
}
