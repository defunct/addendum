package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

class ColumnAlteration implements Update
{
    private final String tableName;
    
    private final String oldName;
    
    private final Column column;
    
    public ColumnAlteration(String tableName, String oldName, Column column)
    {
        this.tableName = tableName;
        this.oldName = oldName;
        this.column = column;
    }

    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        dialect.alterColumn(connection, tableName, oldName, column);
    }
}
