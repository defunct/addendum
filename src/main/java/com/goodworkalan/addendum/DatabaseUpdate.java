package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.danger.Danger;

/**
 * An update performed against the underlying database. Instances of this class
 * are returned by {@link SchemaUpdate#execute(Schema)} to be stored in the
 * {@link Addenda}. Database updates are only run if the addendum accounting in
 * the database indicates that the addendum that contains the database update
 * has not been run.
 * <p>
 * Database update will wrap SQL exceptions as addendum exceptions, if the
 * database update throws an SQL exception. The specific addendum exception
 * error code and error arguments used to create the addendum exception are kept
 * in the database update and specified at construction.
 * 
 * @author Alan Gutierrez
 */
abstract class DatabaseUpdate {
    /** The wrapper exception error code. */
    private final String code;

    /** The wrapper exception arguments. */
    private final Object[] arguments;

    /**
     * Create a database update that will wrap an SQL exception in an addendum
     * exception with the given error code and error arguments.
     * 
     * @param code
     *            The wrapper exception error code.
     * @param arguments
     *            The wrapper exception arguments.
     */
    public DatabaseUpdate(String code, Object...arguments) {
        this.code = code;
        this.arguments = arguments;
    }

    /**
     * Perform the database update using the given JDBC connection and the given
     * dialect wrapping any SQL exception in an addendum exception.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @exception AddendumException
     *                For any error occurring during the update.
     */
    public void update(Connection connection, Dialect dialect) {
        try {
            execute(connection, dialect);
        } catch (SQLException e) {
            throw new Danger(Addendum.class, code, e, arguments);
        }
    }

    /**
     * Perform the database update on the given JDBC connection using the given
     * SQL dialect.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @throws SQLException
     *             For any SQL error.
     * @exception AddendumException
     *                For any error occurring during the update.
     */
    public abstract void execute(Connection connection, Dialect dialect) throws SQLException;
}
