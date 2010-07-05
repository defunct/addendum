package com.goodworkalan.addendum.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.addendum.dialect.Dialect;

/**
 * A mock dialect that can be configured to raise exceptions.
 * 
 * @author Alan Gutierrez
 */
public class MockDialect implements Dialect {
    /** The list of addenda applied. */
    private final List<Integer> addenda = new ArrayList<Integer>();

    /** The create table records. */
    public final List<CreateTable> createTables = new ArrayList<CreateTable>();

    /** The add column records. */
    public final List<AddColumn> addColumns = new ArrayList<AddColumn>();
    
    /** The alter column records. */
    public final List<AlterColumn> alterColumns = new ArrayList<AlterColumn>();
    
    /** Whether to throw an exception when creating the addenda table. */
    private boolean failOnCreateAddendaTable;
    
    /** Whether to throw an exception when selecting the addenda count. */
    private boolean failOnAddendaCount;
    
    /** Whether to throw an exception when inserting an addendum record. */
    private boolean failOnAddendum;
    
    /**
     * Records the addition of an addenda, possibly fails.
     * 
     * @param connection
     *            The JDBC connection.
     * @throws SQLException
     *             For any SQL error.
     */
    public void addendum(Connection connection) throws SQLException {
        if (failOnAddendum) {
            throw new SQLException();
        }
        addenda.add(addenda.size());
    }

    /**
     * Possibly fails.
     * 
     * @param connection
     *            The JDBC connection.
     * @throws SQLException
     *             For any SQL error.
     */
    public void createAddendaTable(Connection connection) throws SQLException {
        if (failOnCreateAddendaTable) {
            throw new SQLException();
        }
    }

    /**
     * Returns the addenda count, possibly throws an exception.
     * 
     * @param connection
     *            An SQL connection on the database.
     * @return The maximum update applied.
     * @throws SQLException
     *             For any SQL error.
     */
    public int addendaCount(Connection connection) throws SQLException {
        if (failOnAddendaCount) {
            throw new SQLException();
        }
        return addenda.size();
    }

    /**
     * Records the column addition.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     */
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException {
        addColumns.add(new AddColumn(tableName, column));
    }

    /**
     * Return this dialect and optionally set a failure state based on the
     * database product name.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The dialect.
     * @throws SQLException
     *             For any SQL error.
     */
   public Dialect canTranslate(Connection connection, Dialect dialect) throws SQLException {
        String databaseName = connection.getMetaData().getDatabaseProductName();
        if (databaseName.equals("ERROR")) {
            throw new SQLException();
        }
        if (databaseName.equals("FAIL_ON_CREATE_ADDENDA_TABLE")) {
            failOnCreateAddendaTable = true;
            return this;
        }
        if (databaseName.equals("FAIL_ON_ADDENDA_COUNT")) {
            failOnAddendaCount = true;
            return this;
        }
        if (databaseName.equals("FAIL_ON_ADDENDUM")) {
            failOnAddendum = true;
            return this;
        }
        if (databaseName.equals("MOCK") && dialect == null) {
            return this;
        }
        return dialect;
    }

    /**
     * Does nothing.
     * 
     * @param tableName
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException {
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param oldName
     *            The exiting column name.
     * @param column
     *            The column definition.
     */
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException {
        alterColumns.add(new AlterColumn(tableName, oldName, column));
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param columnName
     *            The column name.
     */
    public void dropColumn(Connection connection, String tableName, String columnName) throws SQLException {
    }

    /**
     * Does nothing.
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
     */
    public void insert(Connection connection, String table, List<String> columns, List<String> values) {
    }


    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param oldName
     *            The old table name.
     * @param newName
     *            The new table name.
     */
    public void renameTable(Connection connection, String oldName, String newName) throws SQLException {
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void verifyTable(Connection connection, String tableName, List<Column> columns) throws SQLException {
    }
}
