package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class TableAlteration implements Update
{
    private final String tableName;

    private final List<DefineColumn<?, ?>> addColumns;
    
    private final List<DefineColumn<?, ?>> verifyColumns;

    public TableAlteration(String tableName, List<DefineColumn<?, ?>> updateColumns,  List<DefineColumn<?, ?>> verifyColumns)
    {   
        this.tableName = tableName;
        this.addColumns = updateColumns;
        this.verifyColumns = verifyColumns;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        for (DefineColumn<?, ?> column : addColumns)
        {
            dialect.addColumn(connection, tableName, column);
        }
        for (DefineColumn<?, ?> column : verifyColumns)
        {
            dialect.verifyColumn(connection, tableName, column);
        }
    }
}
