package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of {@link Addendum} instances with changes to apply to an
 * application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda
{
    /** This logger is not currently in use. */
    static final Logger log = LoggerFactory.getLogger(Addenda.class);
    
    /** A list of changes to apply to the database. */
    private final List<ApplyAddendum> addenda = new ArrayList<ApplyAddendum>();
    
    /**
     * A list of maps of table definitions by table name, one map for each
     * addendum.
     */
    private final LinkedList<Map<String, Table>> tables = new LinkedList<Map<String, Table>>();
    
    /** A list of verifications to perform after all addenda are complete. */
    private final List<Update> verifications = new ArrayList<Update>();
    
    /**
     * Update versions are stored in the data sources return by this connection
     * server.
     */
    private final Connector connector;
    
    /** The dialect provider. */
    private final DialectProvider dialectProvider;

    /**
     * Create a collection of changes that tracks version updates in the data
     * source returned by the given connection server.
     * 
     * @param connector
     *            The database connection server.
     */
    public Addenda(Connector connector)
    {
        this.connector = connector;
        this.dialectProvider = DialectLibrary.getInstance();
    }

    /**
     * Create a collection of changes that tracks version updates in the data
     * source returned by the given connection server using the given dialect.
     * 
     * @param connector
     *            The database connection server.
     * @param dialect
     *            The dialect.
     */
    public Addenda(Connector connector, Dialect dialect)
    {
        this.connector = connector;
        this.dialectProvider = new DialectInstance(dialect);
    }

    /**
     * Apply all of the addenda if they are not already recored in the addenda
     * table in the database of the associated connector. A dialect will be
     * chosen from the dialects available according to their
     * <code>META-INF/com.goodworkalan.addendum.Dialect</code> files.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void amend() throws SQLException    
    {
        Connection connection = connector.open();
        Dialect dialect;
        try
        {
            dialect = dialectProvider.getDialect(connection);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_CREATE_ADDENDA, e);
        }
        dialect.createAddendaTable(connection);
        int max;
        try
        {
            max = dialect.addendaCount(connection);
        }
        catch (SQLException e)
        {
            throw new AddendumException(AddendumException.SQL_ADDENDA_COUNT, e);
        }
        for (int i = max; i < addenda.size(); i++)
        {
            ApplyAddendum applyAddendum = addenda.get(i); 
            applyAddendum.execute();
            try
            {
                dialect.addendum(connection);
            }
            catch (SQLException e)
            {
                throw new AddendumException(AddendumException.SQL_ADDENDUM, e);
            }
        }
        connector.close(connection);
    }

    /**
     * Create a new addendum that will make zero, one or more changes to a the
     * database associated with the connector of this addenda, or to any other
     * data resources in the application.
     * 
     * @return A domain-specific language element used to specify updates to the
     *         database.
     */
    // FIXME You could mix or match with a descriminator. They would still run in order.
    public Addendum addendum()
    {
        return addendum(connector, dialectProvider);
    }

    /**
     * Create a new addendum that will make zero, one or more changes to a the
     * database associated with the given connector.
     * 
     * @param connector
     *            A database connection server.
     * @return A domain-specific language element used to specify updates to the
     *         database.
     */
    public Addendum addendum(Connector connector)
    {
        return addendum(connector, dialectProvider);
    }

    /**
     * Create a new addendum that will make zero, one or more changes to a the
     * database associated with the given connector using the given dialect.
     * 
     * @param connector
     *            A database connection server.
     * @param dialect
     *            The dialect to use for this addendum.
     * @return A domain-specific language element used to specify updates to the
     *         database.
     */
    public Addendum addendum(Connector connector, Dialect dialect)
    {
        return addendum(connector, new DialectInstance(dialect));
    }

    /**
     * Create a new addendum that will make zero, one or more changes to a the
     * database associated with the given connector using a dialect provided by
     * the givend dialect provider.
     * 
     * @param connector
     *            A database connection server.
     * @param dialect
     *            A provider for the dialect to use for this addendum.
     * @return A domain-specific language element used to specify updates to the
     *         database.
     */
    private Addendum addendum(Connector connector, DialectProvider dialectProvider)
    {
        List<Update> updates = new ArrayList<Update>();
        tables.addFirst(new HashMap<String, Table>());
        ApplyAddendum addendum = new ApplyAddendum(connector, dialectProvider, updates);
        Addendum schema = new Addendum(updates, tables);
        addenda.add(addendum);
        return schema;
    }

    /**
     * After all addenda are applied, verify that the schema matches schema
     * definition specified by a domain-specific language.
     * 
     * @return The root element of a domain-specific language use to specify a
     *         schema verification.
     */
    public Schema verifySchema()
    {
        return new Schema(verifications);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */