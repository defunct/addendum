package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public interface UpdateDatabase {
    /**
     * Perform a database update on the given JDBC connection using the given
     * SQL dialect.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @throws SQLException
     *             For any SQL error.
     * @throws AddendumException
     *             For any error occurring during the update.
     */
    public void execute(Connection connection, Dialect dialect) throws SQLException;
}
