package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.CANNOT_CREATE_TABLE;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;

/**
 * Creates a new entity in the schema.
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
     * Update the schema with a new entity and table and return a database
     * update to create the table in the database.
     * 
     * @param schema
     *            The tracking schema.
     * @return A database update to create the table in the database.
     */
    public DatabaseUpdate execute(Schema schema) {
        schema.aliases.put(entityName, entity.tableName);
        schema.entities.put(entity.tableName, entity);
        return new DatabaseUpdate(CANNOT_CREATE_TABLE, entityName, entity.tableName) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.createTable(connection, entity.tableName, entity.columns.values(), entity.primaryKey);
            }
        };
    }
}
