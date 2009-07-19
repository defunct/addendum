package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TableVerification
{
    private final String tableName;

    private final List<Column> verifyColumns;

    public TableVerification(String tableName, List<Column> updateColumns)
    {   
        this.tableName = tableName;
        this.verifyColumns = updateColumns;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        for (Column column : verifyColumns)
        {
            dialect.verifyColumn(connection, tableName, column);
        }
    }
}
