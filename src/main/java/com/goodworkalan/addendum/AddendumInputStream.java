package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// TODO Document.
public class AddendumInputStream
{
    // TODO Document.
    private final BufferedReader reader;
    
    // TODO Document.
    public AddendumInputStream(InputStream inputStream, String charset) throws UnsupportedEncodingException
    {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }
    
    // TODO Document.
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
            ReadBy readBy = null;
            Class<?> superClass = addendumClass;
            while (readBy == null && superClass != null)
            {
                readBy = superClass.getAnnotation(ReadBy.class);
                superClass = superClass.getSuperclass();
            }
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
