package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of {@link Addendum} instances with changes to apply to 
 * an application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda
{
    /** This logger is not currently in use. */
    static final Logger log = LoggerFactory.getLogger(Addenda.class);
    
    /** A list of changes to apply to the database. */
    private final List<Addendum> updates = new ArrayList<Addendum>();
    
    /**
     * Update versions are stored in the data sources return by this connection
     * server.
     */
    private final Connector connector;

    /**
     * Create a collection of changes that tracks version updates in the data
     * source returned by the given connection server.
     * 
     * @param connections
     *            The database connection server.
     */
    public Addenda(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * Apply all of the addenda if they are not already recored in the
     * addenda table in the database of the associated connector.  
     */
    public void amend()
    {
        Connection connection = connector.open();
        Dialect countDialect;
        try
        {
            countDialect = DialectLibrary.getInstance().getDialect(connection);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_CREATE_ADDENDA, e);
        }
        int max;
        try
        {
            max = countDialect.addendaCount(connection);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_ADDENDA_COUNT, e);
        }
        for (int i = max; i < updates.size(); i++)
        {
            updates.get(i).execute();
            try
            {
                countDialect.addendum(connection);
            }
            catch (SQLException e)
            {
                throw new AddendumException(AddendumException.SQL_ADDENDUM, e);
            }
        }
    }

    /**
     * Add the given addendum to the set of addenda. The addendum will be
     * executed in order, after any addenda added before this adendum, and
     * before any addenda added after this addendum.
     * 
     * @param addendum
     *            The addendum.
     */
    public void add(Addendum addendum)
    {
        updates.add(addendum);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */