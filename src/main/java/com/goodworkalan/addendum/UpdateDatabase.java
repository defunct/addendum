package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

abstract class UpdateDatabase {
    private final Object[] arguments;
    
    private final int code;

    public UpdateDatabase(int code, Object...arguments) {
        this.code = code;
        this.arguments = arguments;
    }
    
    public void update(Connection connection, Dialect dialect) {
        try {
            execute(connection, dialect);
        } catch (SQLException e) {
            throw new AddendumException(code, e, arguments);
        }
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
     * @exception AddendumException
     *                For any error occurring during the update.
     */
    public abstract void execute(Connection connection, Dialect dialect) throws SQLException;
}
