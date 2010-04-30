package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.dialect.Dialect;

/**
 * A collection of {@link Addendum} instances with changes to apply to an
 * application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda {
    /** The tracking schema. */
    private final Schema schema = new Schema();

    /** This logger is not currently in use. */
    static final Logger log = LoggerFactory.getLogger(Addenda.class);
    
    /** A list of changes to apply to the database. */
    private final List<Script> scripts = new ArrayList<Script>();
    
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
    Addenda(Connector connector, DialectProvider dialectProvider) {
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
        this(connector, DialectLibrary.getInstance());
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
        this(connector, new DialectInstance(dialect));
    }

    /**
     * Apply all of the addenda if they are not already recored in the addenda
     * table in the database of the associated connector. A dialect will be
     * chosen from the dialects available according to their
     * <code>META-INF/com.goodworkalan.addendum.Dialect</code> files.
     * 
     * @exception AddendumException
     *                For any SQL error.
     */
    public void amend() {
        Connection connection = connector.open();
        Dialect dialect;
        try {
            dialect = dialectProvider.getDialect(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_GET_DIALECT, e);
        }
        try {
            dialect.createAddendaTable(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_CREATE_ADDENDA, e);
        }
        int max;
        try {
            max = dialect.addendaCount(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_ADDENDA_COUNT, e);
        }
        for (int i = max; i < scripts.size(); i++) {
            Script script = scripts.get(i);
            script.execute();
            try {
                dialect.addendum(connection);
            } catch (SQLException e) {
                throw new AddendumException(SQL_ADDENDUM, e);
            }
        }
        connector.close(connection);
    }

    /**
     * Create a new addendum that will changes to a the database associated with
     * the connector of this addenda, or to any other data resources in the
     * application.
     * 
     * @return An addendum builder used to specify updates to the database.
     */
    // FIXME You could mix or match with a descriminator. They would still run in order.
    // FIXME Would much prefer to register these somewhere else, maybe even name
    // them, then look them up.
    public Addendum addendum() {
        return addendum(connector, dialectProvider);
    }

    /**
     * Create a new addendum that will make changes to a the database associated
     * with the given connector.
     * 
     * @param connector
     *            A database connection provider.
     * @return An addendum builder used to specify updates to the database.
     */
    public Addendum addendum(Connector connector) {
        return addendum(connector, dialectProvider);
    }

    /**
     * Create a new addendum that make changes to the database associated with
     * the given connector using the given dialect.
     * 
     * @param connector
     *            A database connection provider.
     * @param dialect
     *            The dialect to use for this addendum.
     * @return An addendum builder used to specify updates to the database.
     */
    public Addendum addendum(Connector connector, Dialect dialect) {
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
        List<DatabaseUpdate> updates = new ArrayList<DatabaseUpdate>();
        scripts.add(new Script(connector, dialectProvider, updates));
        return new Addendum(new Patch(schema, updates));
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */