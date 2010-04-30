package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * An update action that creates a table in a database.
 * 
 * @author Alan Gutierrez
 */
class TableCreate implements SchemaUpdate {
    /** The entity name. */
    private final String entityName;

    /** The entity definition. */
    private final Entity entity;

    /**
     * Create a new create table update that creates the given entity with the
     * given entity name.
     * 
     * @param entityName
     *            The entity name.
     * @param entity
     *            The entity definition.
     */
    public TableCreate(String entityName, Entity entity) {
        this.entityName = entityName;
        this.entity = entity;
    }

    /**
     * Create a new table on the given JDBC connection using the given SQL
     * dialect.
     * 
     */
    public DatabaseUpdate execute(Schema schema) {
        if (schema.aliases.containsKey(entityName)) {
            throw new AddendumException(ENTITY_EXISTS, entityName);
        }
        schema.aliases.put(entityName, entity.tableName);
        if (schema.entities.containsKey(entity.tableName)) {
            throw new AddendumException(TABLE_EXISTS, entity.tableName);
        }
        schema.entities.put(entity.tableName, entity);
        return new DatabaseUpdate(CANNOT_CREATE_TABLE, entityName, entity.tableName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.createTable(connection, entity.tableName, entity.columns.values(), entity.primaryKey);
            }
        };
    }
}
