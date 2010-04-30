package com.goodworkalan.addendum.connector;

import static com.goodworkalan.addendum.AddendumException.SQL_CLOSE;
import static com.goodworkalan.addendum.AddendumException.SQL_CONNECT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.goodworkalan.addendum.AddendumException;
/**
 * An implementation of a JDBC connection factory that creates connections using
 * the JDBC <code>java.sql.DriverManager</code>.
 * 
 * @author Alan Gutierrez
 */
public class DriverManagerConnector implements Connector {
    /** The url of the JDBC connection. */
    private final String url;

    /** The user name for the JDBC connection. */
    private final String user;

    /** The password for the JDBC connection. */
    private final String password;

    /**
     * Create a new driver manager connector that using the given connect string
     * and identity to connect to a JDBC data source through the JDBC
     * <code>java.sql.DriverManager</code>.
     * 
     * @param url
     *            The connect string.
     * @param user
     *            The user name.
     * @param password
     *            The password.
     */
    public DriverManagerConnector(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Open a connection to a JDBC data source.
     * 
     * @return A JDBC connection.
     */
    public Connection open() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new AddendumException(SQL_CONNECT, e);
        }
    }

    /**
     * Close a JDBC connection created by this connector.
     * 
     * @param connection
     *            A JDBC connection created by this connector.
     */
    public void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new AddendumException(SQL_CLOSE, e);
        }
    }
}
