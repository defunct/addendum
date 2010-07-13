package com.goodworkalan.addendum.connector;

import static com.goodworkalan.addendum.Addendum.NAMING_EXCEPTION;
import static com.goodworkalan.addendum.Addendum.SQL_CLOSE;
import static com.goodworkalan.addendum.Addendum.SQL_CONNECT;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.NamingException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.BadInitialContextFactory;
import com.goodworkalan.addendum.TestInitialContextFactory;
import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link NamingConnector} class.
 *
 * @author Alan Gutierrez
 */
public class NamingConnectorTest {
    /** Test naming exception. */
    @Test(expectedExceptions = Danger.class)
    public void namingException() throws NamingException {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, BadInitialContextFactory.class.getName());
            NamingConnector connector = new NamingConnector("foo");
            connector.open();
        } catch (Danger e) {
            assertEquals(e.code, NAMING_EXCEPTION);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test SQL exception on open. */
    @Test(expectedExceptions = Danger.class)
    public void openSqlException() throws NamingException {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, TestInitialContextFactory.class.getName());
            NamingConnector connector = new NamingConnector("bad");
            connector.open();
        } catch (Danger e) {
            assertEquals(e.code, SQL_CONNECT);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test open. */
    @Test
    public void openAndClose() throws NamingException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, TestInitialContextFactory.class.getName());
        NamingConnector connector = new NamingConnector("good");
        connector.close(connector.open());
    }

    /** Test SQL exception on close. */
    @Test(expectedExceptions = Danger.class)
    public void closeSqlException() throws NamingException, SQLException {
        NamingConnector connector = new NamingConnector("foo");
        Connection connection = mock(Connection.class);
        doThrow(new SQLException()).when(connection).close();
        try {
            connector.close(connection);
        } catch (Danger e) {
            assertEquals(e.code, SQL_CLOSE);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
