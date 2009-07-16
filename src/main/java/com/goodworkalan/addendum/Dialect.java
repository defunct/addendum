package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface Dialect
{
    public void createAddendaTable(Connection connection) throws SQLException;
    
    public int addendaCount(Connection connection) throws SQLException;
    
    public void addendum(Connection connection) throws SQLException;
    
    public boolean canTranslate(Connection connection) throws SQLException;
    
    public String createTable(String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException;
    
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException;
    
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException;
    
    public void verifyColumn(Connection connection, String tableName, Column column) throws SQLException;
    
    public void renameTable(Connection connection, String oldName, String newName) throws SQLException;
    
    public String insert(String table, List<String> columns, List<String> values) throws SQLException;
}
