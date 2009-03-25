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
    // TODO Document.
    public void execute(Connection connection) throws SQLException, AddendumException;
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */