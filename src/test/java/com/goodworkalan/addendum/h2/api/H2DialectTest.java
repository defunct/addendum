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
import com.goodworkalan.addendum.Connector;
import com.goodworkalan.addendum.DriverManagerConnector;

public class H2DialectTest
{
    private Server server;
    
    private File database;
    
    private String getDatabasePath()
    {
        String property = System.getProperty("java.io.tmpdir");
        do
        {
            database = new File(property, "database_" + (int) (Math.random() * 10000));
        }
        while (!database.mkdirs());
        return new File(database, "temp").toString();
    }
    
    @BeforeTest
    public void start() throws SQLException
    {
        server = Server.createTcpServer(new String[] { "-trace" }).start();
    }
    
    @AfterTest
    public void stop()
    {
        server.stop();
    }
    
    @AfterMethod
    public void deleteDatabase()
    {
        if (database != null)
        {
            for (File file : database.listFiles())
            {
                if (!file.delete())
                {
                    throw new RuntimeException();
                }
            }
            if (!database.delete())
            {
                throw new RuntimeException();
            }
        }
        database = null;
    }
    
    private void createPersonAndAddress(Addenda addenda)
    {
        addenda
            .addendum()
                .createTable("Person")
                    .column("firstName", String.class).length(64).end()
                    .column("lastName", String.class).length(64).end()
                    .end()
                .createTable("Address")
                    .column("address", String.class).length(64).end()
                    .column("city", String.class).length(64).end()
                    .column("state", String.class).length(64).end()
                    .column("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez")
                   .end();
    }

    private Connector newConnector(String database)
    {
        return new DriverManagerConnector("jdbc:h2:" + database, "test", "");
    }

    private Addenda newAddenda() throws ClassNotFoundException
    {
        Class.forName("org.h2.Driver");
        return new Addenda(newConnector(getDatabasePath()));
    }

    private void assertTable(Connection connection, String tableName) throws SQLException
    {
        ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
        assertTrue(rs.next());
        rs.close();
    }

    private void assertColumn(Connection connection, String tableName, String name, int type) throws SQLException
    {
        ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, name);
        assertTrue(rs.next());
        assertEquals(rs.getInt("DATA_TYPE"), type);
        rs.close();
    }

    @Test
    public void alterPerson()  throws ClassNotFoundException, SQLException
    {
        Addenda addenda = newAddenda();
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda
            .addendum()
                .alterTable("Person")
                    .alterColumn("firstName").rename("first_name").end()
                    .alterColumn("lastName").rename("last_name").end()
                    .end()
                .commit();
        addenda.amend();
        Connector connector = newConnector(new File(database, "temp").toString());
        Connection connection = connector.open();
        assertTable(connection, "PERSON");
        assertColumn(connection, "PERSON", "FIRST_NAME", Types.VARCHAR);
        assertColumn(connection, "PERSON", "LAST_NAME", Types.VARCHAR);
    }
}
