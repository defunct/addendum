package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Drops a column from a table in the database.
 *
 * @author Alan Gutierrez
 */
public class ColumnDrop implements UpdateSchema {
    /** The table name. */
    private final String tableName;

    /** The name of the property to drop. */
    private final String property;

    /**
     * Drop the column with the given column definition.
     * 
     * @param tableName
     *            The table name.
     * @param columnName
     *            The name of the column to drop.
     */
    public ColumnDrop(String tableName, String property) {
        this.tableName = tableName;
        this.property = property;
    }
    
    public UpdateDatabase execute(Schema schema) {
        Entity entity = schema.tables.get(tableName);
        final String columnName = entity.properties.remove(property);
        if (columnName == null) {
            throw new AddendumException(0, tableName, columnName);
        }
        if (entity.columns.remove(columnName) == null) {
            throw new AddendumException(0, tableName, columnName);
        }
        return new UpdateDatabase() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.dropColumn(connection, tableName, columnName);
            }
        };
    }
}
