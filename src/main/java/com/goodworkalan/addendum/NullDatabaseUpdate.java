package com.goodworkalan.addendum;

import java.sql.Connection;

/**
 * A do nothing implementation for updates that only effect the tracking schema.
 *
 * @author Alan Gutierrez
 */
class NullDatabaseUpdate extends DatabaseUpdate {
    /**
     * Create a do nothing database update.
     */
    public NullDatabaseUpdate() {
        super(0);
    }

    /**
     * Do nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     */
    public void execute(Connection connection, Dialect dialect) {
    }
}
