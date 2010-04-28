package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Perform a table alteration update against the database.
 * 
 * @author Alan Gutierrez
 */
class TableAlteration implements Update
{
    /** The table name. */
    private final String tableName;

    /** The column to create. */
    private final Column column;

    /**
     * Create a table alteration that adds the given columns the given table.
     * 
     * @param tableName
     *            The table name.
     * @param addColumns
     *            The columns to add.
     */
    public TableAlteration(String tableName, Column column)
    {   
        this.tableName = tableName;
        this.column = column;
    }
    
    public void execute(Database database) {
        Table table = database.tables.get(tableName);
        if (table == null) {
            throw new AddendumException(0, tableName);
        }
        if (table.getColumns().put(column.getName(), column) != null) {
            throw new AddendumException(0, tableName, column.getName());
        }
    }

    /**
     * Perform a table alteration that adds columns to a table in the database.
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
            dialect.addColumn(connection, tableName, column);
    }
}
