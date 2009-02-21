/* Copyright Alan Gutierrez 2006 */
/**
 * 
 */
package com.goodworkalan.addendum;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// TODO Document.
public final class AddendumException
extends Exception
{
    // TODO Document.
    private static final long serialVersionUID = 20080620L;

    // TODO Document.
    private final int code;

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

    // TODO Document.
    public int getCode()
    {
        return code;
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */