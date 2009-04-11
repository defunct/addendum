package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Executes any SQL statement against a JDBC database.
 * 
 * @author Alan Gutierrez
 */
public final class SqlAddendum
implements Addendum
{
    /** The connector to the relational database. */
    private final Connector connector;
    
    /** The SQL statement to execute. */
    private final String sql;

    /**
     * Create an SQL addendum that will execute the given SQL statement on the
     * database associated with the given connector.
     * 
     * @param connector
     *            The connector to the relational database.
     * @param sql
     *            The SQL statement to execute.
     */
    public SqlAddendum(Connector connector, String sql)
    {
        if (sql == null)
        {
            throw new NullPointerException();
        }

        this.connector = connector;
        this.sql = sql;
    }

    /**
     * Execute the SQL statement given by the sql property on the relational
     * database associated with the connection property.
     */
    public void execute()
    {
        Statement statement;
        Connection connection = connector.open();
        try
        {
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_EXECUTION, e);
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                throw new AddendumException(AddendumException.SQL_EXECUTION, e);
            }
        }
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */