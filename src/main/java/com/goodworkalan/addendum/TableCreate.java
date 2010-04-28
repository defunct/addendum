package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An update action that creates a table in a database.
 * 
 * @author Alan Gutierrez
 */
class TableCreate implements Update {
    /** The table definition. */
    private final Table table;
    
    /**
     * Create a new create table update action.
     * 
     * @param table
     *            The table definition.
     */
    public TableCreate(Table table) {
        this.table = table;
    }
    
    public void execute(Database database) {
        if (database.tables.containsKey(table.getName())) {
            throw new AddendumException(0, table.getName());
        }
        database.tables.put(table.getName(), table);
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
