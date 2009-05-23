package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * A connector that creates JDBC connections using a JDNI specified data source.
 * 
 * @author Alan Gutierrez
 */
public class NamingConnector implements Connector
{
    /** The JDNI name of the data source. */
    private final String dataSourceName;

    /**
     * Create a connector that will open JDBC connections using the data source
     * resource specified by the given JDNI name.
     * 
     * @param dataSourceName
     *            The JDNI name of the data source.
     */
    public NamingConnector(String dataSourceName)
    {
        this.dataSourceName = dataSourceName;
    }
    
    /**
     * Open a connection to a JDBC data source using a JDNI specified data source.
     * 
     * @return A JDBC connection. 
     */
    public Connection open()
    {
        DataSource dataSource;
        try
        {
            InitialContext context = new InitialContext();
            dataSource = (DataSource) context.lookup(dataSourceName);
        }
        catch (NamingException e)
        {
            throw new AddendumException(AddendumException.NAMING_EXCEPTION, e);
        }
        try
        {
            return dataSource.getConnection();
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_CONNECT, e);
        }
    }
    
    /**
     * Close a JDBC connection created by this connector.
     * 
     * @param connection
     *            A JDBC connection created by this connector.
     */
    public void close(Connection connection)
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_CLOSE, e);
        }
    }
}
