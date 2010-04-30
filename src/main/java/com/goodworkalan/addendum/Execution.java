package com.goodworkalan.addendum;

import java.sql.Connection;
import static com.goodworkalan.addendum.AddendumException.*;
import java.sql.SQLException;

/**
 * An update that will run arbitrary SQL in an {@link Executable}.
 * 
 * @author Alan Gutierrez
 */
class Execution implements UpdateSchema {
    /** The executable to execute. */
    private final Executable executable;

    /**
     * Create a new execution.
     * 
     * @param executable
     *            The executable to execute.
     */
    public Execution(Executable executable) {
        this.executable = executable;
    }

    /**
     * Return a database update that will execute arbitrary SQL statements. This
     * method has no effect on tracking schema.
     * 
     * @param schema
     *            The tracking schema.
     */
    public UpdateDatabase execute(Schema schema) {
        return new UpdateDatabase(CANNOT_EXECUTE_SQL) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                executable.execute(connection, dialect);
            }
        };
    }
}
