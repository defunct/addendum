package com.goodworkalan.addendum;

import java.sql.Connection;

/**
 * A do nothing implementation for updates that only effect the tracking schema.
 *
 * @author Alan Gutierrez
 */
class NullUpdateDatabase implements UpdateDatabase {
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
