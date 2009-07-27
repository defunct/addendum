package com.goodworkalan.addendum.api;

import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.goodworkalan.addendum.Connector;

public class MockConnector implements Connector
{
    public Connection open() throws SQLException
    {
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        Connection connection = mock(Connection.class);
        when(connection.getMetaData()).thenReturn(meta);
        return connection;
    }
    
    
    public void close(Connection connection) throws SQLException
    {
    }
}
