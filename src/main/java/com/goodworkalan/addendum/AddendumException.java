package com.goodworkalan.addendum;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A general purpose exception that indicates that an error occurred in one 
 * of the classes in the sheaf package.
 * <p>
 * FIXME Assign error codes to static constants.
 *   
 * @author Alan Gutierrez
 */
public final class AddendumException
extends Exception
{
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** An I/O exception occurred while reading the dialect resource path. */
    public static int UNABLE_TO_READ_RESOURCE_PATH = 301;
    
    public static int UNABLE_TO_READ_RESOURCE_FILE = 302;
    
    /**
     * Unable to find a dialect class defined in
     * META-INF/services/com.goodworkalan.dialect.Dialect.
     */
    public static int DIALECT_CLASS_NOT_FOUND = 401;
    
    /** A dialect class specified in a  META-INF/services/com.goodworkalan.dialect.Dialect resource does not implement the Dielect interface. */
    public static int NOT_A_DIALECT = 402;

    /** Unable to instantiate a dialect. */
    public static int CANNOT_CREATE_DIALECT = 403;
    
    /** The dialect does not support the specified unique id generator. */
    public static int DIALECT_DOES_NOT_SUPPORT_GENERATOR = 501;
    
    // TODO Document.
    private final int code;

    /** A list of arguments to the formatted error message. */
    private final List<Object> arguments = new ArrayList<Object>();

    // TODO Document.
    public AddendumException(int code, Object... arguments)
    {
        super(message(code, arguments));
        this.code = code;
    }

    // TODO Document.
    public AddendumException(int code, Throwable cause, Object... arguments)
    {
        super(message(code, arguments), cause);
        this.code = code;
    }

    // TODO Document.
    private static String message(Integer code, Object[] arguments)
    {
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.addendum.exceptions");
        ResourceBundle userExceptions = null;
        try
        {
            userExceptions = ResourceBundle.getBundle("/META-INF/com/goodworkalan/addendum/exceptions");
        }
        catch (MissingResourceException e)
        {
        }
        String format = null;
        if ((format = exceptions.getString(code.toString())) == null)
        {
            if (userExceptions != null && (format = userExceptions.getString(code.toString())) == null)
            {
                format = exceptions.getString("no-format");
            }
        }
        return MessageFormat.format(format, arguments);
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
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.sheaf.exceptions");
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