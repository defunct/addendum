package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Drops a column from a table in the database.
 *
 * @author Alan Gutierrez
 */
public class ColumnDrop implements Update {
    /** The table name. */
    private final String tableName;

    /** The name of the column to drop. */
    private final String columnName;

    /**
     * Drop the column with the given column definition.
     * 
     * @param tableName
     *            The table name.
     * @param columnName
     *            The name of the column to drop.
     */
    public ColumnDrop(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    /**
     * Performs a single alter column update against the database.
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
    public void execute(Connection connection, Dialect dialect)
    throws SQLException {
        dialect.dropColumn(connection, tableName, columnName);
    }
}
