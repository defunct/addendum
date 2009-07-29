package com.goodworkalan.addendum.jpa.api;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.Connector;
import com.goodworkalan.addendum.DriverManagerConnector;
import com.goodworkalan.addendum.jpa.CreateEntity;

public class AddendumJpaTest
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
    
    private Connector newConnector(String database)
    {
        return new DriverManagerConnector("jdbc:h2:" + database, "test", "");
    }
    
    private void assertTable(Connector connector, String name) throws SQLException
    {
        Connection connection = connector.open();
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rs = meta.getTables(null, null, name, null);
        assertTrue(rs.next());
        rs.close();
        connector.close(connection);
    }

    @Test
    public void testCreateEntity() throws SQLException
    {
        Connector connector = newConnector(getDatabasePath());
        Addenda addenda = new Addenda(connector);
        addenda
            .addendum()
                .create(new CreateEntity(Person.class)).end()
            .commit();
        addenda.amend();
        assertTable(connector, "PERSON");
    }
}
