package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An update that will run an {@link Executable}.
 * 
 * @author Alan Gutierrez
 */
class Execution implements Update
{
    /** The executable to execute. */
    private final Executable executable;

    /**
     * Create a new execution.
     * 
     * @param executable
     *            The executable to execute.
     */
    public Execution(Executable executable)
    {
        this.executable = executable;
    }

    /**
     * No good way to test an execution since we don't really know what it is
     * going to do.
     * 
     * @param database
     *            The pseudo-database.
     */
    public void execute(Schema database) {
    }

    /**
     * Perform a database update on the given JDBC connection using the given
     * SQL dialect.
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
        executable.execute(connection, dialect);
    }
}
