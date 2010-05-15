package com.goodworkalan.addendum.connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.furnish.Furnish;

public class ConnectorServer {
    /** A service loader for the Dialect service. */
    private Iterable<Dialect> furnishedDialects = new Furnish<Dialect>(Dialect.class);

    /** The map of connector keys to connectors. */
    private final Map<ConnectorKey, Connector> connectors = new LinkedHashMap<ConnectorKey, Connector>();
    
    /** The map of connector keys to dialect providers. */
    private final Map<ConnectorKey, Dialect> dialects = new LinkedHashMap<ConnectorKey, Dialect>();

    /**
     * Create a connection server with the given default connector key, the
     * given default connector that updates the database using the given
     * dialect.
     * 
     * @param connectorKey
     *            The connector key.
     * @param connector
     *            The connector.
     * @param dialect
     *            The dialect.
     */
    public ConnectorServer(ConnectorKey connectorKey, Connector connector, Dialect dialect) {
        connection(connectorKey, connector, dialect);
    }
    
    /**
     * Create a connection server with the given default connector key, the
     * given default connector.
     * 
     * @param connectorKey
     *            The connector key.
     * @param connector
     *            The connector.
     */
    public ConnectorServer(ConnectorKey connectorKey, Connector connector) {
        connection(connectorKey, connector);
    }

    /**
     * Create a copy of the given connector server. Used by {@link Addenda} to 
     * create a defensive copy of a connector server.
     * 
     * @param copy The connector server to copy.
     */
    public ConnectorServer(ConnectorServer copy) {
        connectors.putAll(copy.connectors);
        dialects.putAll(copy.dialects);
    }

    /**
     * Associate the given connection connection with the given connector key.
     * 
     * @param connectorKey
     *            The connector key.
     * @param connector
     *            The connector.
     */
    public void connection(ConnectorKey connectorKey, Connector connector) {
        connectors.put(connectorKey, connector);
    }

    /**
     * Associate the given connection connection with the given connector key
     * and use the given dialect to generate SQL statements.
     * 
     * @param connectorKey
     *            The connector key.
     * @param connector
     *            The connector.
     * @param dialect
     *            The dialect.
     */
    public void connection(ConnectorKey connectorKey, Connector connector, Dialect dialect) {
        connectors.put(connectorKey, connector);
        dialects.put(connectorKey, dialect);
    }

    /**
     * Get the connector key of the default connection for this connection
     * server.
     * 
     * @return The default connector key.
     */
    public ConnectorKey getDefaultConnectorKey() {
        return connectors.keySet().iterator().next();
    }

    /**
     * Get the connector associated with the given connector key.
     * 
     * @param connectorKey
     *            The connector key.
     * @return The connector or null if none is associated with the connector
     *         key.
     */
    public Connector getConnection(ConnectorKey connectorKey) {
        return connectors.get(connectorKey);
    }

    /**
     * Get the dialect associated with the given connector key or null if no
     * specific dialect is associated with the key.
     * 
     * @param connectorKey
     *            The connector key.
     * @return The dialect or null if none is associated with the connector key.
     */
    public Dialect getDialect(ConnectorKey connectorKey) {
        return dialects.get(connectorKey);
    }

    /**
     * Get the SQL dialect for the given connection.
     * 
     * @return The SQL dialect for the given connection.
     */
    public Dialect getDialect(Connection connection) throws SQLException {
        for (Dialect dialect : furnishedDialects) {
            if (dialect.canTranslate(connection)) {
                return dialect;
            }
        }
        return null;
    }
}