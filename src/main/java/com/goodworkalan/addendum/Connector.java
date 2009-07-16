package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Opens and closes a connection to a JDBC data source.
 * 
 * @author Alan Gutierrez
 */
public interface Connector
{
    /**
     * Open a connection to a JDBC data source.
     * 
     * @return A JDBC connection. 
     */
    public Connection open() throws SQLException;

    /**
     * Close a JDBC connection created by this connector.
     * 
     * @param connection
     *            A JDBC connection created by this connector.
     */
    public void close(Connection connection) throws SQLException;
}
