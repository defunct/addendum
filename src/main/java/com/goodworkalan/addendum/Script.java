package com.goodworkalan.addendum;
import static com.goodworkalan.addendum.AddendumException.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A collection of updates to performed on an SQL database or on related data
 * structures in an application.
 * 
 * @author Alan Gutierrez
 */
class Script
{
    /** A factory for JDBC connections. */
    private final Connector connector;
    
    /** A provider for the dialect to use for this addendum. */
    private final DialectProvider dialectProvider;
    
    /** A list of updates to perform. */
    private final List<DatabaseUpdate> updates;

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
    public Script(Connector connector, DialectProvider dialectProvider, List<DatabaseUpdate> updates) {
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
    public void execute() {
        Connection connection = connector.open();
        Dialect dialect;
        try {
            dialect = dialectProvider.getDialect(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_GET_DIALECT, e);
        }
        for (DatabaseUpdate update : updates) {
            update.update(connection, dialect);
        }
        connector.close(connection);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */