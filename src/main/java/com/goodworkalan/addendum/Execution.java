package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

class Execution implements Update
{
    private final Runnable runnable;
    
    public Execution(Runnable runnable)
    {
        this.runnable = runnable;
    }
    
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        runnable.run();
    }
}
