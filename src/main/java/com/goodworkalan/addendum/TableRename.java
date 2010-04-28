package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Performs a single table rename update against the database.
 *
 * @author Alan Gutierrez
 */
class TableRename implements Update
{
    /** The existing table name. */
    private final String name;
    
    /** The new table name. */
    private final String newName;

    /**
     * The existing table name.
     * 
     * @param name
     *            The existing table name.
     * @param newName
     *            The old table name.
     */
    public TableRename(String name, String newName)
    {
        this.name = name;
        this.newName = newName;
    }
    
    public void execute(Database database) {
        Table table = database.tables.remove(name);
        if (table == null) {
            throw new AddendumException(0, name, newName);
        }
        if (database.tables.containsKey(newName)) {
            throw new AddendumException(0, name, newName);
        }
        table.setName(newName);
        database.tables.put(table.getName(), table);
    }

    /**
     * Perform a single table rename update against the database.
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
        dialect.renameTable(connection, name, newName);
    }
}
