package com.goodworkalan.addendum.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import com.goodworkalan.addendum.AddendumException;
import com.goodworkalan.addendum.Column;
import com.goodworkalan.addendum.Dialect;

/**
 * Perform the updates defined by the domain-specific language used by
 * {@link DatabaseAddendum} against a MySQL database.
 * 
 * @author Alan Gutierrez
 */
public class MySQLDialect extends Dialect
{
    /**
     * Create a new MySQL dialect.
     */
    public MySQLDialect()
    {
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
    public void createAddenda(Connection connection) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet results = metaData.getTables(null, null, "Addenda", null);
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
        int max = results.getInt(1);
        results.close();
        return max;
    }
    
    public void addendum(Connection connection) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Addenda(addendum) values(SELECT COALESCE(MAX(addendum), 0) + 1 FROM Addenda)");
    }
    
    /**
     * Return true if the database is a MySQL database.
     * 
     * @return True if the database is a MySQL database.
     */
    public boolean canTranslate(Connection connection) throws SQLException
    {
        return connection.getMetaData().getDatabaseProductName().equals("MySQL");
    }

    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO ").append(table).append("(");
        
        String separator = "";
        
        for (String column : columns)
        {
            sql.append(separator).append(column);
            separator = ", ";
        }
        
        sql.append(")\n");
        
        sql.append("VALUES(");
        
        separator = "";
        for (int i = 0; i < values.size(); i++)
        {
            sql.append(separator).append("?");
            separator = ", ";
        }
        
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        
        for (int i = 0; i < values.size(); i++)
        {
            String value = values.get(i);
            if (value == null)
            {
                statement.setNull(i + 1, Types.VARCHAR);
            }
            else
            {
                statement.setString(i + 1, value);
            }
        }
        
        statement.execute();
        statement.close();
    }
}
