package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnector implements Connector
{
    private final String url;
    
    private final String user;
    
    private final String password;
    
    public DriverManagerConnector(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection open()
    {
        try
        {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_CONNECT, e);
        }
    }
    
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
