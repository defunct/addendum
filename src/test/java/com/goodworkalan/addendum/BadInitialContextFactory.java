package com.goodworkalan.addendum;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * A JNDI initial context factory that raises an exception when an 
 * initial context is requested.
 *
 * @author Alan Gutierrez
 */
public class BadInitialContextFactory implements InitialContextFactory {
    /**
     * Raise a naming exception.
     * 
     * @param environment
     *            The possibly null environment specifying information to be
     *            used in the creation of the initial context.
     * @return Does not return.
     * @exception NamingException
     *                Invariably
     */
    public Context getInitialContext(Hashtable<?, ?> environment)
    throws NamingException {
        throw new NamingException();
    }
}
