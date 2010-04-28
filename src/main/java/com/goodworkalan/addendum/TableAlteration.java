package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Perform a table alteration update against the database.
 * 
 * @author Alan Gutierrez
 */
class TableAlteration implements Update
{
    /** The table name. */
    private final String tableName;

    /** List of columns to create. */
    private final List<Column> addColumns;

    /**
     * Create a table alteration that adds the given columns the given table.
     * 
     * @param tableName
     *            The table name.
     * @param addColumns
     *            The columns to add.
     */
    public TableAlteration(String tableName, List<Column> addColumns)
    {   
        this.tableName = tableName;
        this.addColumns = addColumns;
    }
    
    public void execute(Database database) {
        Table table = database.tables.get(tableName);
        if (table == null) {
            throw new AddendumException(0, tableName);
        }
        for (Column column : addColumns) {
            if (table.getColumns().put(column.getName(), column) != null) {
                throw new AddendumException(0, tableName, column.getName());
            }
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
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        for (Column column : addColumns)
        {
            dialect.addColumn(connection, tableName, column);
        }
    }
}
