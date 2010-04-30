package com.goodworkalan.addendum.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.Column;
import com.goodworkalan.addendum.Dialect;
import com.goodworkalan.addendum.Entity;

public class MockDialect implements Dialect
{
    private final List<Integer> addenda = new ArrayList<Integer>();
    
    private final List<CreateTable> createTables = new ArrayList<CreateTable>();

    private final List<AddColumn> addColumns = new ArrayList<AddColumn>();
    
    private final List<AlterColumn> alterColumns = new ArrayList<AlterColumn>();
    
    public List<CreateTable> getCreateTables()
    {
        return createTables;
    }

    public List<AddColumn> getAddColumns()
    {
        return addColumns;
    }
    
    public List<AlterColumn> getAlterColumns()
    {
        return alterColumns;
    }
    
    public void addendum(Connection connection) throws SQLException
    {
        addenda.add(addenda.size());
    }

    public void createAddendaTable(Connection connection) throws SQLException
    {
    }

    public int addendaCount(Connection connection) throws SQLException
    {
        return addenda.size();
    }

    public void addColumn(Connection connection, String tableName, Column column) throws SQLException
    {
        addColumns.add(new AddColumn(tableName, column));
    }

    public boolean canTranslate(Connection connection) throws SQLException {
        return connection.getMetaData().getDatabaseProductName().equals("MOCK");
    }

    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException
    {
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException
    {
        alterColumns.add(new AlterColumn(tableName, oldName, column));
    }
    
    public void dropColumn(Connection connection, String tableName, String columnName) throws SQLException {
    }

    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException
    {
    }

    public void renameTable(Connection connection, String oldName, String newName) throws SQLException
    {
    }
    
    public void verifyTable(Connection connection, Entity table)
    {
    }
}
