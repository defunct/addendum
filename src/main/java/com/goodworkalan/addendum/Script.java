package com.goodworkalan.addendum;

import java.util.List;

import com.goodworkalan.addendum.connector.ConnectorKey;

/**
 * A collection of updates to performed on an SQL database or on related data
 * structures in an application.
 * 
 * @author Alan Gutierrez
 */
class Script {
    /** The connector key to obtain a JDBC connection factory and SQL dialect. */
    public final ConnectorKey connectorKey;
    
    /** The list of updates to perform. */
    public final List<DatabaseUpdate> updates;

    /**
     * Create a new addendum. The given list of updates is unique to this
     * addendum.
     * 
     * @param connectorKey
     *            The key used to obtain a JDBC connection factory and SQL
     *            dialect.
     * @param updates
     *            A list of updates to perform.
     */
    public Script(ConnectorKey connectorKey, List<DatabaseUpdate> updates) {
        this.connectorKey = connectorKey;
        this.updates = updates;
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */