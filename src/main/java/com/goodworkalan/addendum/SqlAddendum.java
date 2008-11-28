/* Copyright Alan Gutierrez 2006 */
/**
 * 
 */
package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@ReadBy(SqlAddendumReader.class)
public final class SqlAddendum
implements Addendum
{
    private final String sql;
    
    public SqlAddendum(String sql)
    {
        if (sql == null)
        {
            throw new IllegalStateException();
        }

        this.sql = sql;
    }
    
    public void execute(Connection connnection) throws SQLException, AddendumException
    {
        Addendums.log.info("Executing SQL statement <" + sql + ">");
        Statement statement = connnection.createStatement();
        statement.execute(sql);
        statement.close();
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */