package com.goodworkalan.addendum;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

/**
 * An JNDI initial context factory that returns mock data sources to test
 * database connection creation through JNDI.
 * 
 * @author Alan Gutierrez
 */
public class TestInitialContextFactory implements InitialContextFactory {
    /**
     * Return mock data sources with good and bad mock
     * <code>getConnection</code> methods.
     * 
     * @param environment
     *            The possibly null environment specifying information to be
     *            used in the creation of the initial context.
     * @return An initial context.
     */
    public Context getInitialContext(Hashtable<?, ?> environment)
    throws NamingException {
        DataSource bad = mock(DataSource.class);
        try {
            when(bad.getConnection()).thenThrow(new SQLException());
        } catch (SQLException e) {
        }
        InitialContext ic = mock(InitialContext.class);
        when(ic.lookup("bad")).thenReturn(bad);
        DataSource good = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        try {
            when(good.getConnection()).thenReturn(connection);
        } catch (SQLException e) {
        }
        when(ic.lookup("good")).thenReturn(good);
        return ic;
    }
}
