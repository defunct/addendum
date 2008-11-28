package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class AddendumInputStream
{
    private final BufferedReader reader;
    
    public AddendumInputStream(InputStream inputStream, String charset) throws UnsupportedEncodingException
    {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }
    
    public List<Addendum> read() throws AddendumException, IOException
    {
        List<Addendum> addendums = new ArrayList<Addendum>();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            String className = line.trim();
            Class<?> addendumClass;
            try
            {
                addendumClass = Class.forName(className);
            }
            catch (ClassNotFoundException e)
            {
                throw new AddendumException(100, e);
            }
            ReadBy readBy = addendumClass.getAnnotation(ReadBy.class);
            if (readBy == null)
            {
                throw new AddendumException(100);
            }
            AddendumReader addendumReader;
            try
            {
                addendumReader = readBy.value().newInstance();
            }
            catch (Exception e)
            {
                throw new AddendumException(100, e);
            }
            addendums.add(addendumReader.read(addendumClass, reader));
        }
        return addendums;
    }
}
