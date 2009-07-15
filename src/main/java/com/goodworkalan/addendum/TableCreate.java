package com.goodworkalan.addendum;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * An update action that creates a table in a database.
 *  
 * @author Alan Gutierrez
 */
class TableCreate implements Update
{
    /** The table name. */
    private final String name;
    
    /** The list of column definitions. */
    private final List<DefineColumn<?, ?>> columns;
    
    /** The list of primary key fields. */
    private final List<String> primaryKey;

    /**
     * Create a new create table update action.
     * 
     * @param name
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public TableCreate(String name, List<DefineColumn<?, ?>> columns, List<String> primaryKey)
    {
        this.name = name;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    /**
     * Create a new table on the given JDBC connection using the given SQL
     * dialect.
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
        Statement statement = connection.createStatement();
        statement.execute(dialect.createTable(name, columns, primaryKey));
        statement.close();
    }
}