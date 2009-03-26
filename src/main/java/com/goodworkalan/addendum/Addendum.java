package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

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
     * @param connection
     *            A JDBC connection.
     * @throws SQLException
     *             For any SQL error.
     * @throws AddendumException
     *             For any error occuring during the update.
     */
    public void execute(Connection connection) throws SQLException, AddendumException;
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */