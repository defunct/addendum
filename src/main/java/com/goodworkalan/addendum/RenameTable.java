package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public class RenameTable implements Update
{
    private final String oldName;
    
    private final String newName;
    
    public RenameTable(String oldName, String newName)
    {
        this.oldName = oldName;
        this.newName = newName;
    }

    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        dialect.renameTable(oldName, newName);
    }
}
