package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * A collection of {@link Addendum} instances with changes to apply to an
 * application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda {
    private final ReflectiveFactory reflective;
    private final Schema database = new Schema();

    /** This logger is not currently in use. */
    static final Logger log = LoggerFactory.getLogger(Addenda.class);
    
    /** A list of changes to apply to the database. */
    private final List<ApplyAddendum> addenda = new ArrayList<ApplyAddendum>();
    
    /**
     * Update versions are stored in the data sources return by this connection
     * server.
     */
    private final Connector connector;
    
    /** The dialect provider. */
    private final DialectProvider dialectProvider;

    /**
     * Create a collection of changes that tracks version updates in the data
     * source returned by the given connection server using the given dialect.
     * 
     * @param connector
     *            The database connection server.
     * @param dialectProvider
     *            The dialect provider.
     */
    Addenda(ReflectiveFactory reflective, Connector connector, DialectProvider dialectProvider) {
        this.reflective = reflective;
        this.connector = connector;
        this.dialectProvider = dialectProvider;
    }

    /**
     * Create a collection of changes that tracks version updates in the data
     * source returned by the given connection server.
     * 
     * @param connector
     *            The database connection server.
     */
    public Addenda(Connector connector) {
        this(new ReflectiveFactory(), connector ,DialectLibrary.getInstance());
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
    public Addenda(Connector connector, Dialect dialect) {
        this(new ReflectiveFactory(), connector, new DialectInstance(dialect));
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
    
    public Addendum addendum(Class<? extends Definition> definition) {
        Addendum addendum = addendum();
        try {
            reflective.newInstance(definition).define(addendum);
        } catch (ReflectiveException e) {
            throw new AddendumException(0, e);
        }
        return addendum;
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
    private Addendum addendum(Connector connector, DialectProvider dialectProvider) {
        List<Update> updates = new ArrayList<Update>();
        ApplyAddendum addendum = new ApplyAddendum(connector, dialectProvider, updates);
        Addendum schema = new Addendum(new Script(database, updates));
        addenda.add(addendum);
        return schema;
    }
    
    public void create() {
    }

}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */