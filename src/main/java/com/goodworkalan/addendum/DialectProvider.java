package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface to allow late binding to a dialect.
 * 
 * @author Alan Gutierrez
 */
interface DialectProvider {
    /**
     * Get a dialect for the given connection.
     * 
     * @paramS A dialect for the given connection.
     */
    public Dialect getDialect(Connection connection) throws SQLException;
}
