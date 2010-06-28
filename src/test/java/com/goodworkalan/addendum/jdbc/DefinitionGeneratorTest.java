package com.goodworkalan.addendum.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.GeneratorType;
import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.DriverManagerConnector;

/**
 * Unit test for the {@link DefinitionGenerator} class.
 *
 * @author Alan Gutierrez
 */
public class DefinitionGeneratorTest {
    /** An H2 database server. */
    private Server server;
    
    /** An H2 database directory. */
    private File database;

    /**
     * Create an H2 database directory path.
     * 
     * @return The directory as a string.
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
     * Start the H2 database.
     * 
     * @throws SQLException
     *             For any database error.
     */
    @BeforeTest
    public void start() throws SQLException {
        server = Server.createTcpServer(new String[] { "-trace" }).start();
    }

    /** Stop the H2 database. */
    @AfterTest
    public void stop() {
        server.stop();
    }

    /** Delete the H2 database directory. */
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
     * Create an addenda that defines a person and an address.
     * 
     * @param addenda
     *            The addenda.
     */
    private void createPersonAndAddress(Addenda addenda) {
        addenda
            .addendum()
                .create("Person")
                    .add("id", long.class).notNull().generator(GeneratorType.IDENTITY).end()
                    .add("firstName", String.class).length(64).end()
                    .add("lastName", String.class).length(64).end()
                    .primaryKey("id")
                    .end()
                .create("Address")
                    .add("id", long.class).notNull().generator(GeneratorType.IDENTITY).end()
                    .add("address", String.class).length(64).end()
                    .add("city", String.class).length(64).end()
                    .add("state", String.class).length(64).end()
                    .add("zip", String.class).length(64).end()
                    .primaryKey("id")
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez");
    }

    /**
     * Drop the tables created by the invocation of this test.
     * 
     * @param connection
     *            The data source.
     * @throws SQLException
     *             For any SQL error.
     */
    private void dropPersonAndAddress(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE Person");
        } catch (SQLException e) {
        } finally {
            statement.close();
        }
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE Address");
        } catch (SQLException e) {
        } finally {
            statement.close();
        }
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE Addenda");
        } catch (SQLException e) {
        } finally {
            statement.close();
        }
    }

    /**
     * Create a new H2 connector for the given H2 database.
     * 
     * @param database
     *            The database.
     * @return A database connector.
     */
    private Connector newConnector(String database) {
        return new DriverManagerConnector("jdbc:h2:" + database, "test", "");
    }

    /** Test the default constructor. */
    @Test
    public void constructor() throws ClassNotFoundException, SQLException {
        Connector connector = newConnector(getDatabasePath());
        Class.forName("org.h2.Driver");
        Addenda addenda = new Addenda(connector);
        createPersonAndAddress(addenda);
        addenda.amend();
        Connection connection = connector.open();
        DefinitionGenerator.generate("com.goodworkalan.accounts.Accounts", connection);
        connector.close(connection);
        Class.forName("com.mysql.jdbc.Driver"); 
        connector = new DriverManagerConnector("jdbc:mysql://localhost/cds_solidica", "solidica", null);
        addenda = new Addenda(connector);
        connection = connector.open();
        dropPersonAndAddress(connection);
        connector.close(connection);
        createPersonAndAddress(addenda);
        addenda.amend();
        connection = connector.open();
        DefinitionGenerator.generate("com.goodworkalan.accounts.Accounts", connection);
        connector.close(connection);
    }
}
