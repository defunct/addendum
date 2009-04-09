package com.goodworkalan.addendum;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Reads addendum instances from an addendum definition resource.
 * 
 * @author Alan Gutierrez
 */
public interface AddendumReader
{
    /**
     * Read a single addendum of the given addendum class from the given
     * buffered reader of an addendum resource file.
     * 
     * @param addendumClass
     *            The addendum class.
     * @param reader
     *            A buffered reader of and addendum resource file.
     * @return An addendum.
     * @throws AddendumException
     *             For any update error.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public Addendum read(Class<?> addendumClass, BufferedReader reader) throws AddendumException,IOException;
}
