package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class NamingConnector implements Connector
{
    private final String dataSourceName;
    
    public NamingConnector(String dataSourceName)
    {
        this.dataSourceName = dataSourceName;
    }
    
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
}
