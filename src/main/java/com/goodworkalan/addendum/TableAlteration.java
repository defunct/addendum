package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class TableAlteration implements Update
{
    private final String tableName;

    private final List<Column> addColumns;
    
    private final List<Column> verifyColumns;

    public TableAlteration(String tableName, List<Column> updateColumns,  List<Column> verifyColumns)
    {   
        this.tableName = tableName;
        this.addColumns = updateColumns;
        this.verifyColumns = verifyColumns;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        for (Column column : addColumns)
        {
            dialect.addColumn(connection, tableName, column);
        }
        for (Column column : verifyColumns)
        {
            dialect.verifyColumn(connection, tableName, column);
        }
    }
}
