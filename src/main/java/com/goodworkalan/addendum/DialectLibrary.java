package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.furnish.Furnish;

/**
 * The library of available SQL dialects, generated using the service loader
 * pattern.
 * 
 * @author Alan Gutierrez
 */
class DialectLibrary implements DialectProvider  
{
    /** The singleton instance of the library. */
    private final static DialectLibrary INSTNACE = new DialectLibrary(); 

    /** A service loader for the Dialect service. */
    private Iterable<Dialect> dialects = new Furnish<Dialect>(Dialect.class);
    
    /**
     * Create a dialect library.
     */
    private DialectLibrary()
    {
    }

    /**
     * Get the singleton instance of the dialect library.
     * 
     * @return The singleton instance of the dialect library.
     */
    public static DialectLibrary getInstance()
    {
        return INSTNACE;
    }

    /**
     * Get the SQL dialect for the given connection.
     * 
     * @return The SQL dialect for the given connection.
     */
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
