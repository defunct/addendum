package com.goodworkalan.addendum;
import static com.goodworkalan.addendum.AddendumException.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.addendum.dialect.Dialect;

/**
 * An update that adds a column to an existing entity.
 * 
 * @author Alan Gutierrez
 */
class ColumnAdd implements SchemaUpdate
{
    /** The entity table name. */
    private final String tableName;
    
    /** The new property name. */
    private final String property;

    /** The column definition. */
    private final Column column;

    /**
     * Create a table alteration that adds the given columns the given table.
     * 
     * @param tableName
     *            The entity table name.
     * @param property
     *            The new property name.
     * @param column
     *            The column definition.
     */
    public ColumnAdd(String tableName, String property, Column column) {
        this.tableName = tableName;
        this.property = property;
        this.column = column;
    }

    /**
     * Update the schema with the new property and return a database update that
     * will add a column to the table in the database.
     * 
     * @param schema
     *            The tracking schema.
     * @return A column add update to perform against the database.
     */
    public DatabaseUpdate execute(Schema schema) {
        Entity entity = schema.entities.get(tableName);
        entity.properties.put(property, column.getName());
        entity.columns.put(column.getName(), column);
        return new DatabaseUpdate(CANNOT_ADD_COLUMN, column.getName(), tableName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.addColumn(connection, tableName, column);
            }
        };
    }
}
