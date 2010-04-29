package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An update action that creates a table in a database.
 * 
 * @author Alan Gutierrez
 */
class TableCreate implements UpdateSchema {
    private final String alias;

    /** The table definition. */
    private final Entity entity;
    
    /**
     * Create a new create table update action.
     * 
     * @param entity
     *            The table definition.
     */
    public TableCreate(String alias, Entity entity) {
        this.alias = alias;
        this.entity = entity;
    }

    /**
     * Create a new table on the given JDBC connection using the given SQL
     * dialect.
     * 
     */
    public UpdateDatabase execute(Schema schema) {
        if (schema.aliases.containsKey(alias)) {
            throw new AddendumException(0, alias, entity.tableName);
        }
        schema.aliases.put(alias, entity.tableName);
        if (schema.tables.containsKey(entity.tableName)) {
            throw new AddendumException(0, alias, entity.tableName);
        }
        schema.tables.put(entity.tableName, entity);
        return new UpdateDatabase(0) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.createTable(connection, entity.tableName, entity.columns.values(), entity.primaryKey);
            }
        };
    }
}
