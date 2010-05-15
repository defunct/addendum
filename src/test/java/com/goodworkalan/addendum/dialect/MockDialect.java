package com.goodworkalan.addendum.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.addendum.dialect.Dialect;

public class MockDialect implements Dialect
{
    private final List<Integer> addenda = new ArrayList<Integer>();
    
    private final List<CreateTable> createTables = new ArrayList<CreateTable>();

    private final List<AddColumn> addColumns = new ArrayList<AddColumn>();
    
    private final List<AlterColumn> alterColumns = new ArrayList<AlterColumn>();
    
    private boolean failOnCreateAddendaTable;
    
    private boolean failOnAddendaCount;
    
    private boolean failOnAddendum;
    
    public List<CreateTable> getCreateTables() {
        return createTables;
    }

    public List<AddColumn> getAddColumns() {
        return addColumns;
    }
    
    public List<AlterColumn> getAlterColumns() {
        return alterColumns;
    }
    
    public void addendum(Connection connection) throws SQLException {
        if (failOnAddendum) {
            throw new SQLException();
        }
        addenda.add(addenda.size());
    }

    public void createAddendaTable(Connection connection) throws SQLException {
        if (failOnCreateAddendaTable) {
            throw new SQLException();
        }
    }

    public int addendaCount(Connection connection) throws SQLException {
        if (failOnAddendaCount) {
            throw new SQLException();
        }
        return addenda.size();
    }

    public void addColumn(Connection connection, String tableName, Column column) throws SQLException {
        addColumns.add(new AddColumn(tableName, column));
    }

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

    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException {
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException {
        alterColumns.add(new AlterColumn(tableName, oldName, column));
    }
    
    public void dropColumn(Connection connection, String tableName, String columnName) throws SQLException {
    }

    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException {
    }

    public void renameTable(Connection connection, String oldName, String newName) throws SQLException {
    }

    public void verifyTable(Connection connection, String tableName, List<Column> columns) throws SQLException {
    }
}
