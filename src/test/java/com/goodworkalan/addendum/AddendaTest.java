package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;
import static org.testng.Assert.assertEquals;

import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.connector.MockConnector;


/**
 * Unit tests for the {@link Addenda} class.
 *
 * @author Alan Gutierrez
 */
public class AddendaTest {
    /**
     * Run a block of code that raises an <code>AddendumException</code> and
     * assert the values of the exception code and message.
     * 
     * @param runnable
     *            The block of code.
     * @param code
     *            The expected error code.
     * @param message
     *            The expected error message.
     */
    public static void exceptional(Runnable runnable, int code, String message) {
        try {
            runnable.run();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), code);
            assertEquals(e.getMessage(), message);
            throw e;
        }
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = AddendumException.class)
    public void getDialectSQLException() throws SQLException {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("ERROR")).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = AddendumException.class)
    public void getDialectMissing() throws SQLException {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("MISMATCH")).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    /** Test inability to create addenda table. */
    @Test(expectedExceptions = AddendumException.class)
    public void createAddenda() {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("FAIL_ON_CREATE_ADDENDA_TABLE")).amend();

            }
        }, SQL_CREATE_ADDENDA, "Unable to create the addenda table to track updates.");
    }

    /** Test cannot fetch addenda count. */
    @Test(expectedExceptions = AddendumException.class)
    public void addedaCount() {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new MockConnector("FAIL_ON_ADDENDA_COUNT")).amend();
            }
        }, SQL_ADDENDA_COUNT, "Unable to fetch the maximum value of the applied updates from the addenda table.");
    }

    /** Test unable to insert into the addenda table. */
    @Test(expectedExceptions = AddendumException.class)
    public void addendum() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector("FAIL_ON_ADDENDUM"));
                addenda.addendum();
                addenda.amend();
            }
        }, SQL_ADDENDUM, "Unable to insert a new addenda into the the addenda table.");
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
     * Create a new connector with the given H2 database path.
     * 
     * @param database
     *            The database path.
     * @return A new connector.
     */
    @Test
    public void tiny() throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    /** Test the existence of the addenda table. */
    @Test
    public void addendaTableExists()
    throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
    }
    
    /** Test a basic migration. */
    @Test
    public void basic() {
        Addenda addenda = new Addenda(new MockConnector());
        new ExampleMigration(addenda).create();
    }
    
    /** Test skipping. */
    @Test
    public void skip() {
        Addenda addenda = new Addenda(new MockConnector(), 1);
        createPersonAndAddress(addenda);
        addenda.amend();
    }
}

/* vim: set nowrap: */
