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
    
    private final String property;

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
    public TableAlteration(String tableName, String property, Column column)
    {   
        this.tableName = tableName;
        this.property = property;
        this.column = column;
    }
    
    public UpdateDatabase execute(Schema schema) {
        Entity entity = schema.tables.get(tableName);
        if (entity == null) {
            throw new AddendumException(0, tableName);
        }
        if (entity.properties.put(property, column.getName()) != null) {
            throw new AddendumException(0, tableName, column.getName());
        }
        if (entity.columns.put(column.getName(), column) != null) {
            throw new AddendumException(0, tableName, column.getName());
        }
        return new UpdateDatabase() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.addColumn(connection, tableName, column);
            }
        };
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
