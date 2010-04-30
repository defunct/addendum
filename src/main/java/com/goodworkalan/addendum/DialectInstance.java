package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.DIALECT_DOES_NOT_SUPPORT_CONNECTION;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A dialect provider that provides a specific instance of a dialect.
 * 
 * @author Alan Gutierrez
 */
class DialectInstance implements DialectProvider
{
    /** The dialect to provide. */
    private final Dialect dialect;

    /**
     * Create a dialect provider that will provide the given dialect instance.
     * 
     * @param dialect
     *            The dialect to provide.
     */
    public DialectInstance(Dialect dialect) {
        this.dialect = dialect;
    }

    /**
     * Return the dialect instance. If the given connection is not supported by
     * the dialect instance an addendum exception is thrown.
     * 
     * @param connection
     *            The JDBC connection.
     * @return The dialect instance.
     * @exception AddendumException
     *                If the dialect instance does not support the JDBC
     *                connection.
     */
    public Dialect getDialect(Connection connection) throws SQLException {
        if (!dialect.canTranslate(connection)) {
            throw new AddendumException(DIALECT_DOES_NOT_SUPPORT_CONNECTION);
        }
        return dialect;
    }
}
