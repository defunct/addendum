package com.goodworkalan.addendum.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.Column;
import com.goodworkalan.addendum.Dialect;

public class TestDialect implements Dialect
{
    private final List<Integer> addenda = new ArrayList<Integer>();
    
    public void addendum(Connection connection) throws SQLException
    {
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
    }

    public boolean canTranslate(Connection connection) throws SQLException
    {
        return true;
    }

    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException
    {
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException
    {
    }

    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException
    {
    }

    public void renameTable(Connection connection, String oldName, String newName) throws SQLException
    {
    }

    public void verifyColumn(Connection connection, String tableName, Column column) throws SQLException
    {
    }
}
