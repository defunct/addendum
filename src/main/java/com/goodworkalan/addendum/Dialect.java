package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * An implementation of {@link DatabaseAddendeum} database update actions for a
 * specific SQL dialect.
 * 
 * @author Alan Gutierrez
 */
public interface Dialect
{
    /**
     * Determine if the dialect can translate for the given connection.
     * 
     * @param connection
     *            The database connection.
     * @return True if this dialect can translate for the given connection.
     * @throws SQLException
     *             For any SQL error.
     */
    public boolean canTranslate(Connection connection) throws SQLException;
    
    /**
     * Create a table at the given connection with the given name, given columns
     * and the given primary key fields.
     * 
     * @param connection
     *            The database connection.
     * @param name
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public void createTable(Connection connection, String name, List<Column<?, ?>> columns, List<String> primaryKey) throws SQLException, AddendumException;
}
