/* Copyright Alan Gutierrez 2006 */
/**
 * 
 */
package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * TODO ScriptAddendum that would call out to other addendum scripts, so
 * that you could provide scripts for different database languages, but 
 * keep common custom addendums.
 * 
 * @author Alan Gutierrez
 */
@ReadBy(SqlAddendumReader.class)
public final class SqlAddendum
implements Addendum
{
    // TODO Document.
    private final String sql;
    
    // TODO Document.
    public SqlAddendum(String sql)
    {
        if (sql == null)
        {
            throw new IllegalStateException();
        }

        this.sql = sql;
    }
    
    // TODO Document.
    public void execute(Connection connnection) throws SQLException, AddendumException
    {
        Addendums.log.info("Executing SQL statement <" + sql + ">");
        Statement statement = connnection.createStatement();
        statement.execute(sql);
        statement.close();
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */