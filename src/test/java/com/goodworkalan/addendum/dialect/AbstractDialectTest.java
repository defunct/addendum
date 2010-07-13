package com.goodworkalan.addendum.dialect;

import static com.goodworkalan.addendum.Addendum.DIALECT_DOES_NOT_SUPPORT_GENERATOR;
import static com.goodworkalan.addendum.Addendum.DIALECT_DOES_NOT_SUPPORT_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.h2.tools.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.GeneratorType;
import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.DriverManagerConnector;
import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link AbstractDialect} class.
 *
 * @author Alan Gutierrez
 */
public class AbstractDialectTest {
    /** The H2 database server. */
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
        } while (!database.mkdirs());
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
     * Assert that the given table exists in the database at the given
     * connection.
     * 
     * @param connection
     *            The database connection.
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

    /** Test creation of a table. */
    @Test
    public void createTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
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
        columns.add(a);
        dialect.createTable(connection, "A", columns, Collections.<String>emptyList());
        assertTable(connection, "A");
        
        connection.close();
    }

    /** Test definition of a column with a maximum size. */
    @Test
    public void defineText() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", String.class);
        a.setLength(Integer.MAX_VALUE);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a TEXT");
    }

    /** Test definition of a column with an intermediate size. */
    @Test
    public void defineLargerString() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", String.class);
        a.setLength(70000);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a BLURDY(70000)");
    }
    
    /** Test setting the default precision. */
    @Test
    public void defaultPrecision() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.NUMERIC);
        a.setScale(2);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a NUMERIC(10, 2)");
    }
    
    /** Test setting the default scale. */
    @Test
    public void defaultScale() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.NUMERIC);
        a.setPrecision(10);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a NUMERIC(10, 2)");
    }
    
    
    /** Test using a type that is unsupported by the dialect. */
    @Test(expectedExceptions = Danger.class)
    public void unspportedType() {
        try {
            ConcreteDialect dialect = new ConcreteDialect();
            Column a = new Column("a", Types.DECIMAL);
            StringBuilder sql = new StringBuilder();
            dialect.columnDefinition(sql, a, true);
        } catch (Danger e) {
            assertEquals(e.code, DIALECT_DOES_NOT_SUPPORT_TYPE);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test setting not null when not null is not allowed. */
    @Test
    public void cannotNull() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.INTEGER);
        a.setNotNull(true);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, false);
        assertEquals(sql.toString(), "a INTEGER");
    }
    
    /** Test setting not null. */
    @Test
    public void canNullIsNull() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.INTEGER);
        a.setNotNull(true);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a INTEGER NOT NULL");
    }
    
    /** Test identity generator. */
    @Test
    public void identityGenerator() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.INTEGER);
        a.setGeneratorType(GeneratorType.IDENTITY);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a INTEGER AUTO_INCREMENT");
    }
    
    /** Test auto generator. */
    @Test
    public void autoGenerator() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.INTEGER);
        a.setGeneratorType(GeneratorType.AUTO);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a INTEGER AUTO_INCREMENT");
    }
    
    /** Test auto generator. */
    @Test
    public void noneGenerator() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.INTEGER);
        a.setGeneratorType(GeneratorType.NONE);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a INTEGER");
    }
    
    /** Test using a generator that is unsupported by the dialect. */
    @Test(expectedExceptions = Danger.class)
    public void unspportedGenerator() {
        try {
            ConcreteDialect dialect = new ConcreteDialect();
            Column a = new Column("a", Types.INTEGER);
            a.setGeneratorType(GeneratorType.SEQUENCE);
            StringBuilder sql = new StringBuilder();
            dialect.columnDefinition(sql, a, true);
        } catch (Danger e) {
            assertEquals(e.code, DIALECT_DOES_NOT_SUPPORT_GENERATOR);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test identity generator. */
    @Test
    public void defaultValue() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.VARCHAR);
        a.setDefaultValue("'");
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a VARCHAR(225) DEFAULT ''''");
    }
    
    /** Test identity generator. */
    @Test
    public void defaultCharValue() {
        ConcreteDialect dialect = new ConcreteDialect();
        Column a = new Column("a", Types.CHAR);
        a.setDefaultValue("'");
        a.setLength(16);
        StringBuilder sql = new StringBuilder();
        dialect.columnDefinition(sql, a, true);
        assertEquals(sql.toString(), "a CHAR(16) DEFAULT ''''");
    }
    
    /** Test column add. */
    @Test
    public void addColumn() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
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
    
    /** Test column drop. */
    @Test
    public void dropColumn() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        columns.add(a);
        Column b = new Column("b", int.class);
        columns.add(b);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        dialect.dropColumn(connection, "A", "b");

        connection.close();
    }

    /** Test data insert. */
    @Test
    public void insert() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connector connector = newConnector(getDatabasePath());
        Connection connection = connector.open();
        
        ConcreteDialect dialect = new ConcreteDialect();
        List<Column> columns = new ArrayList<Column>();
        Column a = new Column("a", int.class);
        columns.add(a);
        Column b = new Column("b", int.class);
        columns.add(b);
        dialect.createTable(connection, "A", columns, Arrays.asList("a"));
        assertTable(connection, "A");
        
        dialect.insert(connection, "A", Arrays.asList("a", "b"), Arrays.asList("1", null));

        connection.close();
    }
 
    /** Test the unimplemented verify table method. */
    @Test
    public void verifyTable() throws SQLException {
        new ConcreteDialect().verifyTable(null, null, null);
    }
}
