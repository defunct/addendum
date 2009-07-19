package com.goodworkalan.addendum;

import java.sql.Connection;

/**
 * Allows users to specify application specific SQL updates against
 * the database.
 *  
 * @author Alan Gutierrez
 */
public interface Executable
{
    /**
     * Perform an update using application specific SQL statements.
     * 
     * @param connection
     *            An open JDBC connection.
     * @param dialect
     *            The SQL dialect.
     */
    public void execute(Connection connection, Dialect dialect);
}
