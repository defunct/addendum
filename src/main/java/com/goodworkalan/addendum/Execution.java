package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.CANNOT_EXECUTE_SQL;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;

/**
 * An update that will run arbitrary SQL in an {@link Executable}.
 * 
 * @author Alan Gutierrez
 */
class Execution implements SchemaUpdate {
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
    public DatabaseUpdate execute(Schema schema) {
        return new DatabaseUpdate(CANNOT_EXECUTE_SQL) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                executable.execute(connection, dialect);
            }
        };
    }
}
