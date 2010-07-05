package com.goodworkalan.addendum.connector;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.goodworkalan.addendum.connector.Connector;

/**
 * A mock connector.
 *
 * @author Alan Gutierrez
 */
public class MockConnector implements Connector {
    /** The database product name. */
    private final String databaseProductName;
    
    /**
     * Create a mock connection with a database product name of "MOCK".
     */
    public MockConnector() {
        this("MOCK");
    }

    /**
     * Create a mock connector with the given database product name.
     * 
     * @param databaseProductName The database product name.
     */
    public MockConnector(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }
    
    /**
     * Open a mock JDBC connection.
     * 
     * @return A mock JDBC connection.
     */
    public Connection open() {
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        Connection connection = mock(Connection.class);
        try {
            when(connection.getMetaData()).thenReturn(meta);
        } catch (SQLException e) {
        }
        try {
            when(meta.getDatabaseProductName()).thenReturn(databaseProductName);
        } catch (SQLException e) {
        }
        return connection;
    }
    
    
    /**
     * Does nothing.
     * 
     * @param connection
     *            A JDBC connection opened by this connector.
     */
    public void close(Connection connection) {
    }
}
