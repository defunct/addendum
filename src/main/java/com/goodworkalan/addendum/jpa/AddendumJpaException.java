package com.goodworkalan.addendum.jpa;

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
public final class AddendumJpaException
extends RuntimeException
{
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** The error code. */
    private final int code;
    
    /** The specified property was not found in the entity. */
    public final static int PROPERTY_NOT_FOUND = 101;
    
    /** A list of arguments to the formatted error message. */
    private final List<Object> arguments = new ArrayList<Object>();

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public AddendumJpaException(int code)
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
    public AddendumJpaException(int code, Throwable cause)
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
     * @param arguments
     *            The format arguments.
     * @return This sheaf exception for chained invocation of add.
     */
    public AddendumJpaException add(Object...arguments)
    {
        for (Object argument : arguments)
        {
            this.arguments.add(argument);
        }
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
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.addendum.jpa.exceptions");
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