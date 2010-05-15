package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.MISSING_CONNCETOR;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.ConnectorKey;
import com.goodworkalan.addendum.connector.ConnectorServer;
import com.goodworkalan.addendum.dialect.Dialect;

/**
 * A collection of {@link Addendum} instances with changes to apply to an
 * application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda {
    /** The numbers of addendums to skip. */
    private final int skip;

    /** The tracking schema. */
    private final Schema schema = new Schema();

    /** A list of changes to apply to the database. */
    private final List<Script> scripts = new ArrayList<Script>();

    /** The connectors. */
    private final ConnectorServer connectors;

    /**
     * Create a collection of changes that tracks version updates in the data
     * sources returned by the given connection server.
     * 
     * @param connector
     *            The database connection server.
     * @param skip
     *            The number of addenda to always skip.
     */
    public Addenda(ConnectorServer connectors, int skip) {
        this.connectors = new ConnectorServer(connectors);
        this.skip = skip;
    }
    
    /**
     * Create a collection of changes that tracks version updates in the data
     * sources returned by the given connection server.
     * 
     * @param connector
     *            The database connection server.
     */
    public Addenda(ConnectorServer connectors) {
        this(connectors, 0);
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
        setup();
        for (int i = max(), stop = scripts.size(); i < stop; i++) {
            if (i >= skip) {
                run(scripts.get(i));
            }
            increment();
        }
    }
    
    public int max() {
        ConnectorKey connectorKey = connectors.getDefaultConnectorKey();
        Connection connection = open(connectorKey);
        Dialect dialect = getDialect(connectorKey, connection);
        int max;
        try {
            max = dialect.addendaCount(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_ADDENDA_COUNT, e);
        }
        close(connectorKey, connection);
        return max;
    }

    public void setup() {
        ConnectorKey connectorKey = connectors.getDefaultConnectorKey();
        Connection connection = open(connectorKey);
        Dialect dialect = getDialect(connectorKey, connection);
        try {
            dialect.createAddendaTable(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_CREATE_ADDENDA, e);
        }
        close(connectorKey, connection);
    }
    

    /**
     * Perform a single update on the web applications configuration, database
     * or data files possibly using the given configuration.
     * 
     * @throws AddendumException
     *             For any error occurring during the update.
     */
    public void run(Script script) {
        Connection connection = open(script.connectorKey);
        Dialect dialect = getDialect(script.connectorKey, connection);
        for (DatabaseUpdate update : script.updates) {
            update.update(connection, dialect);
        }
        close(script.connectorKey, connection);
    }
    
    public void increment() {
        ConnectorKey connectorKey = connectors.getDefaultConnectorKey();
        Connection connection = open(connectorKey);
        Dialect dialect = getDialect(connectorKey, connection);
        try {
            dialect.addendum(connection);
        } catch (SQLException e) {
            throw new AddendumException(SQL_ADDENDUM, e);
        }
        close(connectorKey, connection);
    }
    
    public Connection open(ConnectorKey connectorKey) {
        Connector connector = connectors.getConnection(connectorKey);
        if (connector == null) {
            throw new AddendumException(MISSING_CONNCETOR);
        }
        return connector.open();
    }
    
    public Dialect getDialect(ConnectorKey connectorKey, Connection connection) {
        Dialect dialect = connectors.getDialect(connectorKey);
        if (dialect == null) {
            try {
                dialect = connectors.getDialect(connection);
            } catch (SQLException e) {
                throw new AddendumException(SQL_GET_DIALECT, e);
            }
            if (dialect == null) {
                throw new AddendumException(SQL_GET_DIALECT);
            }
        }
        return dialect;
    }
    
    public void close(ConnectorKey connectorKey, Connection connection) {
        connectors.getConnection(connectorKey).close(connection);
    }

    /**
     * Create a new addendum that will changes to a the database associated with
     * the connector of this addenda, or to any other data resources in the
     * application.
     * 
     * @return An addendum builder used to specify updates to the database.
     */
    public Addendum addendum() {
        return addendum(connectors.getDefaultConnectorKey());
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
    public Addendum addendum(ConnectorKey connectorKey) {
        List<DatabaseUpdate> updates = new ArrayList<DatabaseUpdate>();
        scripts.add(new Script(connectorKey, updates));
        return new Addendum(new Patch(schema, updates));
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */