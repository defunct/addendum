package com.goodworkalan.addendum;


/**
 * Performs a single update an aspect of a web applicaiton's configuration,
 * database or data files.
 * 
 * @author Alan Gutierrez
 */
public interface Addendum
{
    /**
     * Perform a single update on the web applications configuration, database
     * or data files possibly using the given configuration.
     * 
     * @throws AddendumException
     *             For any error occuring during the update.
     */
    public void execute() throws AddendumException;
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */