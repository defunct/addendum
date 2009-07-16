package com.goodworkalan.addendum.api;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.goodworkalan.addendum.Dialect;

public class H2Dialect extends Dialect
{
    /**
     * Create a new MySQL dialect.
     */
    public H2Dialect()
    {
        super();
        setType(Types.BIT, "BIT");
        setType(Types.BOOLEAN, "BIT");
        setType(Types.TINYINT, "TINYINT");
        setType(Types.SMALLINT, "SMALLINT");
        setType(Types.INTEGER, "INTEGER");
        setType(Types.BIGINT, "BIGINT");
        setType(Types.FLOAT, "FLOAT");
        setType(Types.DOUBLE, "DOUBLE");
        setType(Types.VARCHAR, "VARCHAR(%1$d)", 65535);
        setType(Types.VARCHAR, "TEXT");
        setType(Types.TIMESTAMP, "TIMESTAMP");
        setType(Types.NUMERIC, "NUMERIC(%2$d, %3$d)");
    }
    
    /**
     * Create the table used to store the applied addenda.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void createAddendaTable(Connection connection) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet results = metaData.getTables(null, null, "ADDENDA", null);
        boolean build = !results.next();
        results.close();
        if (build)
        {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE Addenda (addendum INTEGER)");
            statement.close();
        }
    }
    
    public int addendaCount(Connection connection) throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT COALESCE(MAX(addendum), 0) FROM Addenda");
        results.next();
        int max = results.getInt(1);
        results.close();
        return max;
    }
    
    public void addendum(Connection connection) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Addenda SELECT COALESCE(MAX(addendum), 0) + 1 FROM Addenda");
        statement.close();
    }
    
    /**
     * Return true if the database is a MySQL database.
     * 
     * @return True if the database is a MySQL database.
     */
    public boolean canTranslate(Connection connection) throws SQLException
    {
        return connection.getMetaData().getDatabaseProductName().equals("H2");
    }
}