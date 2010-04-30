package com.goodworkalan.addendum;

import java.sql.Connection;
import static com.goodworkalan.addendum.AddendumException.*;

import java.sql.SQLException;

/**
 * Performs a single alter column update against the database.
 *
 * @author Alan Gutierrez
 */
class ColumnAlteration implements UpdateSchema
{
    /** The entity table name. */
    private final String tableName;
    
    /** The existing column name. */
    private final String propertyName;
    
    /** The column definition. */
    private final Column column;

    /**
     * Create a column alteration with the given table name, the given existing
     * column name and the given column definition.
     * 
     * @param tableName
     *            The entity table name.
     * @param oldProperty
     *            The existing column name.
     * @param column
     *            The column definition.
     */
    public ColumnAlteration(String tableName, String propertyName, Column column)
    {
        this.tableName = tableName;
        this.propertyName = propertyName;
        this.column = column;
    }

    /**
     * Update schema with new column definition, updating the property mapping
     * if the column has been renamed. Return a database update that will alter
     * and possibly rename the column.
     * 
     * @param schema
     *            The tracking schema.
     * @return A column alteration database update.
     */
    public UpdateDatabase execute(Schema schema) {
        Entity entity = schema.entities.get(tableName);
        final String oldColumnName = entity.properties.get(propertyName);
        if (!column.getName().equals(oldColumnName)) {
            entity.columns.remove(entity.properties.remove(propertyName));
            entity.properties.put(propertyName, column.getName());
        }
        entity.columns.put(column.getName(), column);
        final String tableName = entity.tableName;
        return new UpdateDatabase(CANNOT_ALTER_COLUMN, tableName, oldColumnName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.alterColumn(connection, tableName, oldColumnName, column);
            }
        };
    }
}
