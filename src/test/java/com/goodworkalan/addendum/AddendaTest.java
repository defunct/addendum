package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.Addendum.SQL_ADDENDUM;
import static com.goodworkalan.addendum.Addendum.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.Addendum.SQL_GET_DIALECT;
import static org.testng.AssertJUnit.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.goodworkalan.addendum.connector.MockConnector;
import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.addendum.dialect.MockDatabase;
import com.goodworkalan.danger.Danger;
import com.goodworkalan.danger.test.Dangerous;


/**
 * Unit tests for the {@link Addenda} class.
 *
 * @author Alan Gutierrez
 */
public class AddendaTest {
    /** Reset the mock database before running a test. */
    @BeforeMethod
    public void resetDatabase() {
        MockDatabase.clear();
    }

    /** The constructor creates a new instance of an addenda. */ 
    @Test
    public void constructor() {
        new Addenda(new MockConnector());
    }

    /**
     * Run a block of code that raises an <code>Danger</code> and assert the
     * values of the exception code and message.
     * 
     * @param runnable
     *            The block of code.
     * @param code
     *            The expected error code.
     * @param message
     *            The expected error message.
     */
    public static void exceptional(Runnable runnable, String code, String message) {
        try {
            runnable.run();
        } catch (Danger e) {
            assertEquals(e.code, code);
            assertEquals(e.getMessage(), message);
            throw e;
        }
    }
    
    /**
     * An <code>SQLException</code> thrown from
     * {@link Dialect#canTranslate(java.sql.Connection, Dialect) is wrapped in a
     * <code>Danger</code> and thrown.
     */
    @Test(expectedExceptions = Danger.class)
    public void getDialectSQLException() throws SQLException {
        Dangerous.danger(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("ERROR")).amend();
            }
        }, Addendum.class, SQL_GET_DIALECT, SQLException.class, "Unable to create the database dialect.");
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = Danger.class)
    public void getDialectMissing() throws SQLException {
        Dangerous.danger(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("MISMATCH")).amend();
            }
        }, Addendum.class, SQL_GET_DIALECT, null, "Unable to create the database dialect.");
    }
    
    /**
     * When the Addenda table cannot be created a <code>Danger</code> exception
     * is thrown.
     */
    @Test(expectedExceptions = Danger.class)
    public void unableToCreateAddendaTable() {
        Dangerous.danger(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("FAIL_ON_CREATE_ADDENDA_TABLE")).amend();

            }
        }, Addendum.class, SQL_CREATE_ADDENDA, SQLException.class, "Unable to create the addenda table to track updates.");
    }

    /**
     * When the addenda count cannot be fetched from the Addenda table a
     * <code>Danger</code> exception is thrown.
     */
    @Test(expectedExceptions = Danger.class)
    public void unableToGetAddedaCount() {
        Dangerous.danger(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("FAIL_ON_ADDENDA_COUNT")).amend();
            }
        }, Addendum.class, SQL_ADDENDA_COUNT, SQLException.class, "Unable to fetch the maximum value of the applied updates from the addenda table.");
    }

    /**
     * When the addenda count cannot be fetched from the Addenda table a
     * <code>Danger</code> exception is thrown.
     */
    @Test(expectedExceptions = Danger.class)
    public void unableToInsertAddendum() {
        Dangerous.danger(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector("FAIL_ON_ADDENDUM"));
                addenda.addendum();
                addenda.amend();
            }
        }, Addendum.class, SQL_ADDENDUM, SQLException.class, "Unable to insert a new addenda into the the addenda table.");
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
    
    /**
     * An empty amend completes successfully without throwing exceptions.
     */
    @Test
    public void emptyAmend() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda.addendum();
        addenda.amend();
        assertEquals(1, MockDatabase.INSTANCE.addenda.size());
    }

    /**
     * Create a new connector with the given H2 database path.
     * 
     * @param database
     *            The database path.
     * @return A new connector.
     */
 // @Test
    public void tiny() throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    /** Test the existence of the addenda table. */
 //  @Test
    public void addendaTableExists()
    throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
    }
    
    /** Test a basic migration. */
 //  @Test
    public void basic() {
        Addenda addenda = new Addenda(new MockConnector());
        new ExampleMigration(addenda).create();
    }
    
    /**
     * A skipped addendum is not invoked. If the skipped addendum were invoked,
     * an exception would be thrown by this test.
     */
    @Test
    public void skip() {
        Addenda addenda = new Addenda(new MockConnector(), 1);
        addenda
            .addendum()
                .execute(new Executable() {
                    public void execute(Connection connection, Dialect dialect) {
                        throw new RuntimeException();
                    }
                })
            .commit();
        addenda.amend();
        assertEquals(1, MockDatabase.INSTANCE.addenda.size());
    }
}

/* vim: set nowrap: */
