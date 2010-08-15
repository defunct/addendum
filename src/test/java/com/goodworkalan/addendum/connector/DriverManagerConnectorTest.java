package com.goodworkalan.addendum.connector;

import static com.goodworkalan.addendum.Addendum.SQL_CLOSE;
import static com.goodworkalan.addendum.Addendum.SQL_CONNECT;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.testng.annotations.Test;

import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link DriverManagerConnector} class.
 *
 * @author Alan Gutierrez
 */
public class DriverManagerConnectorTest {
    /** Test SQL exception on open. */
 //  @Test(expectedExceptions = Danger.class)
    public void openSqlException() throws NamingException, SQLException {
        DriverManagerConnector connector = new DriverManagerConnector(null, null, null);
        try {
            connector.open();
        } catch (Danger e) {
            assertEquals(e.code, SQL_CONNECT);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test SQL exception on close. */
 //   @Test(expectedExceptions = Danger.class)
    public void closeSqlException() throws NamingException, SQLException {
        DriverManagerConnector connector = new DriverManagerConnector(null, null, null);
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
    
    /** Test close. */
 //   @Test
    public void close() throws NamingException, SQLException {
        DriverManagerConnector connector = new DriverManagerConnector(null, null, null);
        Connection connection = mock(Connection.class);
        connector.close(connection);
    }
}
