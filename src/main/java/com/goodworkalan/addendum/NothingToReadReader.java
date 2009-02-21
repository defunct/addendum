package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;

// TODO Document.
public class NothingToReadReader implements AddendumReader
{
    // TODO Document.
    public Addendum read(Class<?> addendumClass, BufferedReader reader) throws AddendumException, IOException
    {
        try
        {
            return (Addendum) addendumClass.newInstance();
        }
        catch (Exception e)
        {
            throw new AddendumException(100, e);
        }
    }
}
