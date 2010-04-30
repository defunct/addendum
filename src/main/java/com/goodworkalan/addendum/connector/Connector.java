package com.goodworkalan.addendum.connector;

import java.sql.Connection;

/**
 * Opens and closes a connection to a JDBC data source.
 * 
 * @author Alan Gutierrez
 */
public interface Connector {
    /**
     * Open a connection to a JDBC data source.
     * 
     * @return A JDBC connection.
     */
    public Connection open();

    /**
     * Close a JDBC connection created by this connector.
     * 
     * @param connection
     *            A JDBC connection created by this connector.
     */
    public void close(Connection connection);
}
