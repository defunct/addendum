package com.goodworkalan.addendum;

import com.goodworkalan.addendum.Column;

public class AlterColumn
{
    private final String tableName;
    
    private final String oldName;
    
    private final Column column;
    
    public AlterColumn(String tableName, String oldName, Column column)
    {
        this.tableName = tableName;
        this.oldName = oldName;
        this.column = column;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public String getOldName()
    {
        return oldName;
    }
    
    public Column getColumn()
    {
        return column;
    }
}
