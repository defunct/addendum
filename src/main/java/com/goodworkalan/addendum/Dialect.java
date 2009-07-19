package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * An implementation of the creation and execution of the SQL DDL for a
 * particular dialect of SQL.
 * 
 * @author Alan Gutierrez
 */
public interface Dialect
{
    /**
     * Create the table used to store the applied addenda.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void createAddendaTable(Connection connection) throws SQLException;
    
    /**
     * Get the maximum update applied.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @return The maximum update applied.
     * @throws SQLException
     *             For any SQL error.
     */
    public int addendaCount(Connection connection) throws SQLException;
    
    /**
     * Increment the update number in the database.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void addendum(Connection connection) throws SQLException;
    
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
     * Create a table with the given table name, given columns and the given
     * primary key fields.
     * 
     * @param tableName
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException;
    
    /**
     * Add a the given column definition to the the given table.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException;

    /**
     * Alter the column in the given table with the given exiting column name
     * according to the given column definition. This method can both rename and
     * redefine columns.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param oldName
     *            The exiting column name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException;

    public void verifyTable(Connection connection, String tableName);
    
    /**
     * Verify that a column in the given table has the given column definition.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void verifyColumn(Connection connection, String tableName, Column column) throws SQLException;
    
    /**
     * Rename a table form the given old name to the given new name.
     * 
     * @param connection
     *            The JDBC connection.
     * @param oldName
     *            The old table name.
     * @param newName
     *            The new table name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void renameTable(Connection connection, String oldName, String newName) throws SQLException;
    
    /**
     * Insert a row into the given table. The column names are specified by the
     * given columns list. The values are specified by the given values list.
     * 
     * @param connection
     *            The database connection.
     * @param table
     *            The name of the table to insert into.
     * @param columns
     *            The name of insert columns.
     * @param values
     *            A parallel list of insert values, parallel to the insert
     *            columns.
     * @throws SQLException
     *             For any SQL error.
     */
    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException;
}
