package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;


public interface AddendumReader
{
    public Addendum read(Class<?> addendumClass, BufferedReader reader) throws AddendumException,IOException;
}
