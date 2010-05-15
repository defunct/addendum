package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;
import static org.testng.Assert.assertEquals;

import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.connector.MockConnector;


public class AddendaTest {
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
                new Addenda(new DatabaseMetaDataConnector("ERROR")).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    /** Test the failure of the creation of a {@link Definition}. */
    @Test(expectedExceptions = AddendumException.class)
    public void getDialectMissing() throws SQLException {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new DatabaseMetaDataConnector("MISMATCH")).amend();
            }
        }, SQL_GET_DIALECT, "Unable to create the database dialect.");
    }
    
    @Test(expectedExceptions = AddendumException.class)
    public void createAddenda() {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new DatabaseMetaDataConnector("FAIL_ON_CREATE_ADDENDA_TABLE")).amend();

            }
        }, SQL_CREATE_ADDENDA, "Unable to create the addenda table to track updates.");
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addedaCount() {
        exceptional(new Runnable() {
            public void run() {
                new Addenda(new DatabaseMetaDataConnector("FAIL_ON_ADDENDA_COUNT")).amend();
            }
        }, SQL_ADDENDA_COUNT, "Unable to fetch the maximum value of the applied updates from the addenda table.");
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addendum() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new DatabaseMetaDataConnector("FAIL_ON_ADDENDUM"));
                addenda.addendum();
                addenda.amend();
            }
        }, SQL_ADDENDUM, "Unable to update the addenda table with a new update.");
    }

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

    @Test
    public void tiny() throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    @Test
    public void addendaTableExists()
    throws ClassNotFoundException, SQLException {
        Addenda addenda = new Addenda(new MockConnector());
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
    }
    
    @Test
    public void basic() {
        Addenda addenda = new Addenda(new MockConnector());
        new ExampleMigration(addenda).create();
    }
    
    @Test
    public void skip() {
        Addenda addenda = new Addenda(new MockConnector(), 1);
        createPersonAndAddress(addenda);
        addenda.amend();
    }
}

/* vim: set nowrap: */
