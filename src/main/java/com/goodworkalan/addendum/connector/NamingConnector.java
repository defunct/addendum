package com.goodworkalan.addendum.connector;

import static com.goodworkalan.addendum.Addendum.NAMING_EXCEPTION;
import static com.goodworkalan.addendum.Addendum.SQL_CLOSE;
import static com.goodworkalan.addendum.Addendum.SQL_CONNECT;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.goodworkalan.addendum.Addendum;
import com.goodworkalan.danger.Danger;

/**
 * A connector that creates JDBC connections using a JDNI specified data source.
 * 
 * @author Alan Gutierrez
 */
public class NamingConnector implements Connector {
    /** The JDNI name of the data source. */
    private final String dataSourceName;

    /**
     * Create a connector that will open JDBC connections using the data source
     * resource specified by the given JDNI name.
     * 
     * @param dataSourceName
     *            The JDNI name of the data source.
     */
    public NamingConnector(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * Open a connection to a JDBC data source using a JDNI specified data
     * source.
     * 
     * @return A JDBC connection.
     */
    public Connection open() {
        DataSource dataSource;
        try {
            InitialContext context = new InitialContext();
            dataSource = (DataSource) context.lookup(dataSourceName);
        } catch (NamingException e) {
            throw new Danger(Addendum.class, NAMING_EXCEPTION, e, dataSourceName);
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new Danger(Addendum.class, SQL_CONNECT, e);
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
            throw new Danger(Addendum.class, SQL_CLOSE, e);
        }
    }
}
