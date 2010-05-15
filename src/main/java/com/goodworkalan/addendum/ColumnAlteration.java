package com.goodworkalan.addendum;

import java.sql.Connection;
import static com.goodworkalan.addendum.AddendumException.*;

import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.addendum.dialect.Dialect;

/**
 * Performs a single alter column update against the database.
 *
 * @author Alan Gutierrez
 */
class ColumnAlteration implements SchemaUpdate {
    /** The entity table name. */
    private final String tableName;
    
    /** The new column name. */
    private final String newColumnName;
    
    /** The column definition. */
    private final Column column;

    /**
     * Create a column alteration with the given table name, the given existing
     * column name and the given column definition.
     * 
     * @param tableName
     *            The entity table name.
     * @param column
     *            The column definition.
     * @param newColumnName
     *            The new column name.
     */
    public ColumnAlteration(String tableName, Column column, String newColumnName) {
        this.tableName = tableName;
        this.column = column;
        this.newColumnName = newColumnName;
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
    public DatabaseUpdate execute(Schema schema) {
        Entity entity = schema.entities.get(tableName);
        final String oldColumnName = column.getName(); 
        if (!oldColumnName.equals(newColumnName)) {
            String propertyName = entity.getPropertyName(oldColumnName);
            entity.columns.remove(oldColumnName);
            column.setName(newColumnName);
            entity.properties.put(propertyName, newColumnName);
            if (entity.columns.containsKey(newColumnName)) {
                throw new AddendumException(COLUMN_EXISTS, column.getName());
            }
            entity.columns.put(newColumnName, column);
        }
        final String tableName = entity.tableName;
        final Column frozenColumn = new Column(column);
        return new DatabaseUpdate(CANNOT_ALTER_COLUMN, tableName, oldColumnName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.alterColumn(connection, tableName, oldColumnName, frozenColumn);
            }
        };
    }
}
