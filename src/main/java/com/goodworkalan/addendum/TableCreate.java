package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An update action that creates a table in a database.
 * 
 * @author Alan Gutierrez
 */
class TableCreate implements Update {
    private final String alias;

    /** The table definition. */
    private final Table table;
    
    /**
     * Create a new create table update action.
     * 
     * @param table
     *            The table definition.
     */
    public TableCreate(String alias, Table table) {
        this.alias = alias;
        this.table = table;
    }
    
    public void execute(Database schema) {
        if (schema.aliases.containsKey(alias)) {
            throw new AddendumException(0, alias, table.getName());
        }
        schema.aliases.put(alias, table.getName());
        if (schema.tables.containsKey(table.getName())) {
            throw new AddendumException(0, alias, table.getName());
        }
        schema.tables.put(table.getName(), table);
    }

    /**
     * Create a new table on the given JDBC connection using the given SQL
     * dialect.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @throws SQLException
     *             For any SQL error.
     * @throws AddendumException
     *             For any error occurring during the update.
     */
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        dialect.createTable(connection, table.getName(), table.getColumns().values(), table.getPrimaryKey());
    }
}
