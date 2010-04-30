package com.goodworkalan.addendum.dialect;

import com.goodworkalan.addendum.dialect.Column;

public class AddColumn
{
    private final String tableName;
    
    private final Column column;
    
    public AddColumn(String tableName, Column column)
    {
        this.tableName = tableName;
        this.column = column;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public Column getColumn()
    {
        return column;
    }
}
