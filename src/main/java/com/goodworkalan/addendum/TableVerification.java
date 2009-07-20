package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Verify the existence of a table definition in the database.
 * 
 * @author Alan Gutierrez
 */
public class TableVerification implements Update
{
    /** The table defintion. */
    private final Table table;

    /**
     * Create a new table verification.
     * 
     * @param table
     *            The table definition.
     */
    public TableVerification(Table table)
    {   
        this.table = table;
    }

    /**
     * Perform a verification of the existence of a table definition in the
     * database.
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
        dialect.verifyTable(connection, table);
    }
}
