package com.goodworkalan.addendum.api;

import java.io.File;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
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

    @Test
    public void addendum() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.h2.Driver");
        
        Addenda addenda = new Addenda(new DriverManagerConnector("jdbc:h2:" + getDatabasePath(), "test", ""));
        
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
        
        addenda.amend();
        
        addenda.amend();
    }
}
