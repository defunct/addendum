package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.CANNOT_DROP_COLUMN;
import static com.goodworkalan.addendum.Addendum.PROPERTY_MISSING;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.danger.Danger;

/**
 * Drops a property from an entity in the database.
 *
 * @author Alan Gutierrez
 */
class ColumnDrop implements SchemaUpdate {
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

    /**
     * Drop the property in the tracking schema and return a database update
     * that will drop the column in the database.
     * 
     * @param schema
     *            The tracking schema.
     * @return A database update that will drop the column in the database.
     * @exception Danger If the property does not exist.
     */
    public DatabaseUpdate execute(Schema schema) {
        Entity entity = schema.entities.get(tableName);
        final String columnName = entity.properties.remove(property);
        if (columnName == null) {
            throw new Danger(Addendum.class, PROPERTY_MISSING, property);
        }
        entity.columns.remove(columnName);
        return new DatabaseUpdate(CANNOT_DROP_COLUMN, columnName, tableName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.dropColumn(connection, tableName, columnName);
            }
        };
    }
}
