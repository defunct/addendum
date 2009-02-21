package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;

// TODO Document.
public interface AddendumReader
{
    // TODO Document.
    public Addendum read(Class<?> addendumClass, BufferedReader reader) throws AddendumException,IOException;
}
