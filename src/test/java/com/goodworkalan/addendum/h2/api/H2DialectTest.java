package com.goodworkalan.addendum.h2.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.DriverManagerConnector;
import com.goodworkalan.addendum.h2.H2Dialect;

/**
 * Unit tests for the {@link H2Dialect}.
 *
 * @author Alan Gutierrez
 */
public class H2DialectTest {
    /** The H2 serfver. */
    private Server server;
    
    /** The database directory. */
    private File database;
    
    /**
     * Create a temporary directory to store the H2 database.
     * 
     * @return A temporary directory.
     */
    private String getDatabasePath() {
        String property = System.getProperty("java.io.tmpdir");
        do {
            database = new File(property, "database_" + (int) (Math.random() * 10000));
        }
        while (!database.mkdirs());
        return new File(database, "temp").toString();
    }
    
    /**
     * Start the database server.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    @BeforeTest
    public void start() throws SQLException {
        server = Server.createTcpServer(new String[] { "-trace" }).start();
    }

    /** Stop the database server. */
    @AfterTest
    public void stop() {
        server.stop();
    }

    /** Delete the working database directory. */
    @AfterMethod
    public void deleteDatabase() {
        if (database != null) {
            for (File file : database.listFiles()) {
                if (!file.delete()) {
                    throw new RuntimeException();
                }
            }
            if (!database.delete()) {
                throw new RuntimeException();
            }
        }
        database = null;
    }
    
    /**
     * Create person and address entity definitions.
     * 
     * @param addenda
     *            The addenda.
     */
    private void createPersonAndAddress(Addenda addenda) {
        addenda
            .addendum()
                .create("Person")
                    .add("firstName", String.class).length(64).end()
                    .add("lastName", String.class).length(64).end()
                    .end()
                .create("Address")
                    .add("address", String.class).length(64).end()
                    .add("city", String.class).length(64).end()
                    .add("state", String.class).length(64).end()
                    .add("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez");
    }

    /**
     * Create a new connector with the given H2 database path.
     * 
     * @param database
     *            The database path.
     * @return A new connector.
     */
    private Connector newConnector(String database) {
        return new DriverManagerConnector("jdbc:h2:" + database, "test", "");
    }

    /**
     * Create a new addenda on an H2 database.
     * 
     * @return A new addenda.
     * @throws ClassNotFoundException
     *             If the H2 JDBC driver cannot be loaded.
     */
    private Addenda newAddenda() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        return new Addenda(newConnector(getDatabasePath()));
    }

    /**
     * Assert that the table exists.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @throws SQLException
     *             For any SQL error.
     */
    private void assertTable(Connection connection, String tableName)
    throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
        assertTrue(rs.next());
        rs.close();
    }

    /**
     * Assert that the given column of the given type exists in the given table.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param name
     *            The name.
     * @param type
     *            The type.
     * @throws SQLException
     *             For any SQL error.
     */
    private void assertColumn(Connection connection, String tableName, String name, int type)
    throws SQLException {
        ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, name);
        assertTrue(rs.next());
        assertEquals(rs.getInt("DATA_TYPE"), type);
        rs.close();
    }

    /** Test table alter. */
    @Test
    public void alterPerson() throws ClassNotFoundException, SQLException {
        Addenda addenda = newAddenda();
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda
            .addendum()
                .alter("Person")
                    .alter("firstName").column("first_name").end()
                    .alter("lastName").column("last_name").end()
                    .end()
                .alter("Address")
                    .alter("city").length(128).end()
                    .end()
                .commit();
        addenda.amend();
        Connector connector = newConnector(new File(database, "temp").toString());
        Connection connection = connector.open();
        assertTable(connection, "PERSON");
        assertColumn(connection, "PERSON", "FIRST_NAME", Types.VARCHAR);
        assertColumn(connection, "PERSON", "LAST_NAME", Types.VARCHAR);
    }
    
    /** Test table name. */
    @Test
    public void renamePerson() throws ClassNotFoundException, SQLException {
        Addenda addenda = newAddenda();
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda
            .addendum()
                .rename("Person", "Human")
                .commit();
        addenda.amend();
        Connector connector = newConnector(new File(database, "temp").toString());
        Connection connection = connector.open();
        assertTable(connection, "HUMAN");
    }
}
