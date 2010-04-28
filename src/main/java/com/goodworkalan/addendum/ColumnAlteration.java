package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Performs a single alter column update against the database.
 *
 * @author Alan Gutierrez
 */
class ColumnAlteration implements Update
{
    /** The table name. */
    private final String tableName;
    
    /** The existing column name. */
    private final String columnName;
    
    /** The column definition. */
    private final Column column;

    /**
     * Create a column alteration with the given table name, the given existing
     * column name and the given column definition.
     * 
     * @param tableName
     *            The table name.
     * @param oldName
     *            The existing column name.
     * @param column
     *            The column definition.
     */
    public ColumnAlteration(String tableName, String oldName, Column column)
    {
        this.tableName = tableName;
        this.columnName = oldName;
        this.column = column;
    }
    
    public void execute(Database database) {
        Table table = database.tables.get(tableName);
        if (table == null) {
            throw new AddendumException(0, tableName, columnName);
        }
        if (table.getColumns().remove(columnName) == null) {
            throw new AddendumException(0, tableName, columnName);
        }
        table.getColumns().put(column.getName(), column);
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
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        dialect.alterColumn(connection, tableName, columnName, column);
    }
}
