package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.furnish.Furnish;

public class DialectLibrary  
{
    public final static DialectLibrary INSTNACE = new DialectLibrary(); 

    private Iterable<Dialect> dialects = new Furnish<Dialect>(Dialect.class);
    
    private DialectLibrary()
    {
    }
    
    public Dialect getDialect(Connection connection) throws SQLException
    {
        for (Dialect dialect : dialects)
        {
            if (dialect.canTranslate(connection))
            {
                return dialect;
            }
        }
        return null;
    }
}
