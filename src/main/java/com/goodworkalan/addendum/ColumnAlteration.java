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
    /** The entity name. */
    private final String entityName;
    
    /** The existing column name. */
    private final String propertyName;
    
    /** The column definition. */
    private final Column column;

    /**
     * Create a column alteration with the given table name, the given existing
     * column name and the given column definition.
     * 
     * @param tableName
     *            The entity name.
     * @param oldProperty
     *            The existing column name.
     * @param column
     *            The column definition.
     */
    public ColumnAlteration(String entityName, String propertyName, Column column)
    {
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.column = column;
    }
    
    public UpdateDatabase execute(Schema schema) {
        Entity entity = schema.getEntity(entityName);
        final String oldColumnName = entity.properties.get(propertyName);
        if (!column.getName().equals(oldColumnName)) {
            entity.columns.remove(entity.properties.remove(propertyName));
            entity.properties.put(propertyName, column.getName());
        }
        entity.columns.put(column.getName(), column);
        final String tableName = entity.tableName;
        return new UpdateDatabase() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.alterColumn(connection, tableName, oldColumnName, column);
            }
        };
    }
}
