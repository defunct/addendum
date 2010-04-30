package com.goodworkalan.addendum;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    /** Test creation of a table. */
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
    
    /** Test creation of a table with no primary key. */
    @Test
    public void createTableNoPrimaryKey() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        a.setDefaults(int.class);
        columns.add(a);
        dialect.createTable(connection, "A", columns, Collections.<String>emptyList());
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
    
    @Test
    public void defineText() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", String.class);
        a.setDefaults(String.class);
        a.setLength(Integer.MAX_VALUE);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a TEXT");
    }

    @Test
    public void defineLargerString() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", String.class);
        a.setDefaults(String.class);
        a.setLength(70000);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a BLURDY(70000)");
    }

    @Test
    public void addColumn() throws SQLException, ClassNotFoundException {
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
        
        a.setName("b");
        dialect.addColumn(connection, "A", a);

        a.setName("c");
        a.setNotNull(true);
        a.setDefaultValue(0);
        dialect.addColumn(connection, "A", a);

        connection.close();
    }
    

    @Test
    public void dropColumn() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        a.setDefaults(int.class);
        columns.add(a);
        Column b = new Column("b", int.class);
        b.setDefaults(int.class);
        columns.add(b);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        dialect.dropColumn(connection, "A", "b");

        connection.close();
    }

    @Test
    public void insert() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        a.setDefaults(int.class);
        columns.add(a);
        Column b = new Column("b", int.class);
        b.setDefaults(int.class);
        columns.add(b);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        dialect.insert(connection, "A", Arrays.asList("a", "b"), Arrays.asList("1", null));

        connection.close();
    }
}
