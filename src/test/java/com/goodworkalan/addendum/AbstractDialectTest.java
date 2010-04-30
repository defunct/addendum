package com.goodworkalan.addendum;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AbstractDialectTest {
    private Server server;
    
    private File database;
    
    private String getDatabasePath() {
        String property = System.getProperty("java.io.tmpdir");
        do {
            database = new File(property, "database_" + (int) (Math.random() * 10000));
        } while (!database.mkdirs());
        return new File(database, "temp").toString();
    }

    @BeforeTest
    public void start() throws SQLException {
        server = Server.createTcpServer(new String[] { "-trace" }).start();
    }

    @AfterTest
    public void stop() {
        server.stop();
    }
    
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
    
    private Connector newConnector(String database) {
        return new DriverManagerConnector("jdbc:h2:" + database, "test", "");
    }
    
    private void assertTable(Connection connection, String tableName)
    throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
        assertTrue(rs.next());
        rs.close();
    }

    @Test
    public void createTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        a.setDefaults(int.class);
        columns.add(a);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        connection.close();
    }
    
    @Test
    public void getMetaColumn() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        a.setDefaults(int.class);
        columns.add(a);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        dialect.getMetaColumn(connection, "A", "A");
        
        connection.close();
    }
}
