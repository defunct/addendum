package com.agtrz.addendum;

import java.io.BufferedReader;
import java.io.IOException;


public class SqlAddendumReader implements AddendumReader
{
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
