package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class TableAlteration implements Update
{
    private final String tableName;

    private final List<Column> addColumns;

    public TableAlteration(String tableName, List<Column> updateColumns)
    {   
        this.tableName = tableName;
        this.addColumns = updateColumns;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        for (Column column : addColumns)
        {
            dialect.addColumn(connection, tableName, column);
        }
    }
}