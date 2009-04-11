package com.goodworkalan.addendum.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class MySQLDialect implements Dialect
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

    /**
     * Create a table at the given connection with the given name, given columns
     * and the given primary key fields.
     * 
     * @param connection
     *            The database connection.
     * @param name
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public void createTable(Connection connection, String name, List<Column<?, ?>> columns, List<String> primaryKey) throws SQLException, AddendumException
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("CREATE TABLE ").append(name).append(" (\n");
        
        String separator = "";
        for (Column<?, ?> column : columns)
        {
            sql.append(separator);
            sql.append(column.getName()).append(" ");
        
            int length = 0;
            boolean precision = false;
            
            switch (column.getColumnType())
            {
            case INTEGER:
                sql.append("INTEGER");
                break;
            case VARCHAR:
                sql.append("VARCHAR");
                length = 255;
                break;
            case NUMBER:
                sql.append("NUMBER");
                length = 255;
                precision = true;
                break;
            case TEXT:
                sql.append("TEXT");
                break;
            }
            
            if (length != 0)
            {
                if (column.getLength() != 0)
                {
                    length = column.getLength();
                }

                if (precision)
                {
                    
                }
                else
                {
                    sql.append("(").append(length).append(")");
                }
            }
            
            if (column.isNotNull())
            {
                sql.append(" NOT NULL");
            }
            
            switch (column.getGeneratorType())
            {
            case NONE:
                break;
            case PREFERRED:
            case AUTO_INCREMENT:
                sql.append(" AUTO_INCREMENT");
                break;
            case SEQUENCE:
                throw new AddendumException(AddendumException.DIALECT_DOES_NOT_SUPPORT_GENERATOR).add("SEQUENCE");
            }
            
            separator = ",\n";
        }
        
        if (!primaryKey.isEmpty())
        {
            sql.append(separator);
            sql.append("PRIMARY KEY (");
            String keySeparator = "";
            for (String key : primaryKey)
            {
                sql.append(keySeparator).append(key);
                keySeparator = ", ";
            }
            sql.append(")");
        }

        sql.append("\n)");
        
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
        statement.close();
    }
}
