package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.ConnectorKey;
import com.goodworkalan.addendum.connector.ConnectorServer;
import com.goodworkalan.addendum.connector.MockConnector;
import com.goodworkalan.addendum.dialect.MockDialect;


public class AddendaTest {
    public static void exceptional(Runnable runnable, int code, String message) {
        try {
            runnable.run();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), code);
            assertEquals(e.getMessage(), message);
            throw e;
        }
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = AddendumException.class)
    public void getDialectSQLException() throws SQLException {
        final Connection connection = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(meta.getDatabaseProductName()).thenReturn("ERROR");
        when(connection.getMetaData()).thenReturn(meta);
        final ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new Connector() {
            public Connection open() {
                return connection;
            }
            
            public void close(Connection connection) {
            }
        });
        exceptional(new Runnable() {
            public void run() {
                new Addenda(connectors).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = AddendumException.class)
    public void getDialectMissing() throws SQLException {
        final Connection connection = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(meta.getDatabaseProductName()).thenReturn("MISMATCH");
        when(connection.getMetaData()).thenReturn(meta);
        final ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new Connector() {
            public Connection open() {
                return connection;
            }
            
            public void close(Connection connection) {
            }
        });
        exceptional(new Runnable() {
            public void run() {
                new Addenda(connectors).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    /** Test requesting a missing connector. */
    @Test(expectedExceptions = AddendumException.class)
    public void missingConnector() {
        exceptional(new Runnable() {
            public void run() {
                ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect());
                Addenda addenda = new Addenda(connectors);
                addenda.addendum(new ConnectorKey());
                addenda.amend();
            }
        }, MISSING_CONNCETOR, "Unable to find a connector for a connector key.");
    }

    @Test(expectedExceptions = AddendumException.class)
    public void createAddenda() {
        exceptional(new Runnable() {
            public void run() {
                ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect() {
                    @Override
                    public void createAddendaTable(Connection connection)
                    throws SQLException {
                        throw new SQLException();
                    }
                });
                new Addenda(connectors).amend();
            }
        }, SQL_CREATE_ADDENDA, "Unable to create the addenda table to track updates.");
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addedaCount() {
        exceptional(new Runnable() {
            public void run() {
                ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect() {
                    @Override
                    public int addendaCount(Connection connection)
                    throws SQLException {
                        throw new SQLException();
                    }
                });
                new Addenda(connectors).amend();
            }
        }, SQL_ADDENDA_COUNT, "Unable to fetch the maximum value of the applied updates from the addenda table.");
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addendum() {
        exceptional(new Runnable() {
            public void run() {
                ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect() {
                    @Override
                    public void addendum(Connection connection)
                    throws SQLException {
                        throw new SQLException();
                    }
                });
                Addenda addenda = new Addenda(connectors);
                addenda.addendum();
                addenda.amend();
            }
        }, SQL_ADDENDUM, "Unable to update the addenda table with a new update.");
    }

    private void createPersonAndAddress(Addenda addenda) {
        addenda
            .addendum()
                .define("Person")
                    .add("firstName", String.class).length(64).end()
                    .add("lastName", String.class).length(64).end()
                    .end()
                .define("Address")
                    .add("address", String.class).length(64).end()
                    .add("city", String.class).length(64).end()
                    .add("state", String.class).length(64).end()
                    .add("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez");
    }

    @Test
    public void tiny() throws ClassNotFoundException, SQLException {
        ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect());
        Addenda addenda = new Addenda(connectors);
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    @Test
    public void addendaTableExists()
    throws ClassNotFoundException, SQLException {
        ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect());
        Addenda addenda = new Addenda(connectors);
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
    }
    
    @Test
    public void basic() {
        ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector());
        connectors.connection(ExampleMigration.ALTERNATE, new MockConnector());
        Addenda addenda = new Addenda(connectors);
        new ExampleMigration(addenda).create();
    }
    
    @Test
    public void skip() {
        ConnectorServer connectors = new ConnectorServer(new ConnectorKey(), new MockConnector(), new MockDialect());
        Addenda addenda = new Addenda(connectors, 1);
        createPersonAndAddress(addenda);
        addenda.amend();
    }
}

/* vim: set nowrap: */
