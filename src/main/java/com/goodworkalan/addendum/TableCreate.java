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
    private final Table table;
    
    /** The list of primary key fields. */
    private final List<String> primaryKey;

    /**
     * Create a new create table update action.
     * 
     * @param table
     *            The table definition.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public TableCreate(Table table, List<String> primaryKey)
    {
        this.table = table;
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
        statement.execute(dialect.createTable(table.getName(), table.getColumns().values(), primaryKey));
        statement.close();
    }
}
