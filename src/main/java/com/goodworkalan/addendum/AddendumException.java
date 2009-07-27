package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A general purpose exception that indicates that an error occurred in one 
 * of the classes in the sheaf package.
 *   
 * @author Alan Gutierrez
 */
public final class AddendumException
extends RuntimeException
{
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** The error code. */
    private final int code;

    /** The requested generator type is not supported by the SQL dialect. */
    public final static int DIALECT_DOES_NOT_SUPPORT_GENERATOR = 101;
    
    /** The dialog does not support a specific SQL type. */
    public final static int DIALECT_DOES_NOT_SUPPORT_TYPE = 102;
    
    /**
     * A specific dialect instance was used, but that dialect does not support
     * the database at the other end of the JDBC connection.
     */
    public final static int DIALECT_DOES_NOT_SUPPORT_CONNECTION = 103;
    
    /** Unable to open an SQL connection due to a JNI naming error. */
    public final static int NAMING_EXCEPTION = 201;
    
    /** Unable to connect to a JDBC data source. */
    public final static int SQL_CONNECT = 301;
    
    /** Unable to create the update table to track updates. */
    public final static int SQL_CREATE_ADDENDA = 302;
    
    /** Unable to determine the maximum value of the applied updates. */
    public final static int SQL_ADDENDA_COUNT = 303;
    
    /** Unable to update the addenda table with a new addendum. */
    public final static int SQL_ADDENDUM = 304;
    
    /** Unable to execute a SQL migration statement. */
    public final static int SQL_EXECUTION = 308;
    
    /** Unable to close a JDBC data source. */
    public final static int SQL_CLOSE = 399;
    
    /** Insert statement DSL values count does not match column count. */
    public final static int INSERT_VALUES = 401;
    
    /** A list of arguments to the formatted error message. */
    private final List<Object> arguments = new ArrayList<Object>();

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public AddendumException(int code)
    {
        super();
        this.code = code;
    }

    /**
     * Wrap the given cause exception in an addendum exception with the given
     * error code.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause exception.
     */
    public AddendumException(int code, Throwable cause)
    {
        super(cause);
        this.code = code;
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * Add an argument to the list of arguments to provide the formatted error
     * message associated with the error code.
     * 
     * @param argument
     *            The format argument.
     * @return This sheaf exception for chained invocation of add.
     */
    public AddendumException add(Object argument)
    {
        arguments.add(argument);
        return this;
    }

    /**
     * Create an detail message from the error message format associated with
     * the error code and the format arguments.
     * 
     * @return The exception message.
     */
    @Override
    public String getMessage()
    {
        String key = Integer.toString(code);
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.addendum.exceptions");
        String format;
        try
        {
            format = exceptions.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }
        try
        {
            return String.format(format, arguments.toArray());
        }
        catch (Throwable e)
        {
            throw new Error(key, e);
        }
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */