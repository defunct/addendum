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
     * Perform a database update on the given JDBC connection using the given
     * SQL dialect.
     * 
     * @param schema
     *            The pseudo-database.
     */
    public UpdateDatabase execute(Schema schema) {
        return new UpdateDatabase() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                executable.execute(connection, dialect);
            }
        };
    }
}
