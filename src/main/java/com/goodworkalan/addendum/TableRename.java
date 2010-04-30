package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;

import static com.goodworkalan.addendum.AddendumException.*;

/**
 * Rename a table in the schema.
 * 
 * @author Alan Gutierrez
 */
class TableRename implements SchemaUpdate {
    /** The entity name. */
    private final String alias;
    
    /** The existing table name. */
    private final String from;

    /** The new table name. */
    private final String to;

    /**
     * The existing table name.
     * 
     * @param entityName
     *            The entity name.
     * @param from
     *            The existing table name.
     * @param to
     *            The new table name.
     */
    public TableRename(String entityName, String from, String to) {
        this.alias = entityName;
        this.from = from;
        this.to = to;
    }

    /**
     * Rename a table in the tracking schema and return a database update that
     * renames a table in the database.
     * 
     * @param schema
     *            The tracking schema.
     * @return A database update that renames a table.
     */
    public DatabaseUpdate execute(Schema schema) {
        Entity entity = schema.entities.remove(from);
        entity.tableName = to;
        schema.entities.put(to, entity);
        schema.aliases.put(alias, to);
        return new DatabaseUpdate(CANNOT_RENAME_TABLE, from, to) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.renameTable(connection, from, to);
            }
        };
    }
}
