package com.goodworkalan.addendum.api;

import java.io.File;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.Connector;
import com.goodworkalan.addendum.DriverManagerConnector;


public class AddendaTest
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

    @Test
    public void addendum() throws ClassNotFoundException, SQLException
    {
        Addenda addenda = newAddenda();
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    @Test
    public void addendaTableExists() throws ClassNotFoundException, SQLException
    {
        Addenda addenda = newAddenda();
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
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
    }
}

/* vim: set nowrap: */
