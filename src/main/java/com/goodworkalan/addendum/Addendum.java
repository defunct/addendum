package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A collection of updates to performed on an SQL database or on related data
 * structures in an application.
 * 
 * @author Alan Gutierrez
 */
class Addendum
{
    private final Connector connector;
    
    /** A list of updates to perform. */
    private final List<Update> updates;
    
    public Addendum(Connector connector, List<Update> updates)
    {
        this.connector = connector;
        this.updates = updates;
    }

    /**
     * Perform a single update on the web applications configuration, database
     * or data files possibly using the given configuration.
     * 
     * @throws AddendumException
     *             For any error occurring during the update.
     */
    public void execute() throws SQLException
    {
        Connection connection = connector.open();
        Dialect dialect = DialectLibrary.getInstance().getDialect(connection);
        for (Update update : updates)
        {
            update.execute(connection, dialect);
        }
        connector.close(connection);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */