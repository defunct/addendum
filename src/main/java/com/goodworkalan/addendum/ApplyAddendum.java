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
class ApplyAddendum
{
    /** A factory for JDBC connections. */
    private final Connector connector;
    
    /** A provider for the dialect to use for this addendum. */
    private final DialectProvider dialectProvider;
    
    /** A list of updates to perform. */
    private final List<UpdateDatabase> updates;

    /**
     * Create a new addendum. The given list of updates is unique to this
     * addendum.
     * 
     * @param connector
     *            A factory for JDBC connections.
     * @param dialectProvider
     *            A provider for the dialect to use for this addendum.
     * @param updates
     *            A list of updates to perform.
     */
    public ApplyAddendum(Connector connector, DialectProvider dialectProvider, List<UpdateDatabase> updates)
    {
        this.connector = connector;
        this.dialectProvider = dialectProvider;
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
        Dialect dialect = dialectProvider.getDialect(connection);
        for (UpdateDatabase update : updates)
        {
            update.execute(connection, dialect);
        }
        connector.close(connection);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */