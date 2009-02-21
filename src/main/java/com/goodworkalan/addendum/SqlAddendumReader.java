package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;


// TODO Document.
public class SqlAddendumReader implements AddendumReader
{
    // TODO Document.
    public Addendum read(Class<?> addendumClass, BufferedReader reader) throws IOException
    {
        StringBuilder newString = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null && !line.trim().equals("\\g"))
        {
            newString.append(line).append("\n");
        }
        return new SqlAddendum(newString.toString());
    }
}
