/* Copyright Alan Gutierrez 2006 */
/**
 * 
 */
package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public interface Addendum
{
    public void execute(Connection connnection) throws SQLException, AddendumException;
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */