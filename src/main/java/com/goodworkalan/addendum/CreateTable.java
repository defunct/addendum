package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CreateTable implements Update
{
    private final String name;
    
    private final List<Column<?, ?>> columns;
    
    private final List<String> primaryKey;
    
    public CreateTable(String name, List<Column<?, ?>> columns, List<String> primaryKey)
    {
        this.name = name;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException, AddendumException
    {
        dialect.createTable(connection, name, columns, primaryKey);
    }
}
