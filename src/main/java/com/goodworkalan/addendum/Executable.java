package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;

/**
 * Allows users to specify application specific SQL updates against the
 * database.
 * 
 * @author Alan Gutierrez
 */
public interface Executable {
    /**
     * Perform an update using application specific SQL statements.
     * 
     * @param connection
     *            An open JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @throws SQLException
     *             For any SQL error.
     */
    public void execute(Connection connection, Dialect dialect)
    throws SQLException;
}
