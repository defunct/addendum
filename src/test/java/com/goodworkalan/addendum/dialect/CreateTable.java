package com.goodworkalan.addendum.dialect;

import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.dialect.Column;

public class CreateTable
{
    public final String tableName;
    
    public final Collection<Column> columns;
    
    public final List<String> primaryKey;
    
    public CreateTable(String tableName, Collection<Column> columns, List<String> primaryKey)
    {
        this.tableName = tableName;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public Collection<Column> getColumns()
    {
        return columns;
    }
    
    public List<String> getPrimaryKey()
    {
        return primaryKey;
    }
}
