package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Performs a single table rename update against the database.
 *
 * @author Alan Gutierrez
 */
class RenameTable implements Update
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
    public RenameTable(String name, String newName)
    {
        this.name = name;
        this.newName = newName;
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
