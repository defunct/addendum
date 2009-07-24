package com.goodworkalan.addendum.h2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.goodworkalan.addendum.AbstractDialect;
import com.goodworkalan.addendum.Column;
import com.goodworkalan.prattle.Log;
import com.goodworkalan.prattle.Logger;
import com.goodworkalan.prattle.LoggerFactory;

public class H2Dialect extends AbstractDialect
{
    private final static Logger logger = LoggerFactory.getLogger(H2Dialect.class);

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
    
    @Override
    protected String columnCase(String columnName)
    {
        return columnName.toUpperCase();
    }
    
    @Override
    protected String tableCase(String tableName)
    {
        return tableName.toUpperCase();
    }
    
    @Override
    protected Logger getLogger()
    {
        return logger;
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

    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException
    {
        Log debug = logger.debug();
        try
        {
            debug.message("Altering column %s in table %s.", column.getName(), tableName);
            
            debug.string("tableName", tableName).string("oldName", oldName).freeze("column", column);
            
            if (!oldName.equals(column.getName()))
            {
                String sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + oldName + " RENAME TO " + column.getName();
                
                debug.object("rename", sql);
                
                Statement statement = connection.createStatement();
                statement.execute(sql);
                statement.close();
            }
            
            boolean altered = column.getColumnType() != null
                           || column.getLength() != null
                           || column.getPrecision() != null
                           || column.getScale() != null
                           || column.isNotNull() != null
                           || column.getGeneratorType() != null
                           || column.getHasDefaultValue();
    
            if (altered)
            {
                Column meta = getMetaColumn(connection, tableName, column.getName());
                
                debug.object("meta", meta);
                
                inherit(column, meta);
    
                StringBuilder sql = new StringBuilder();
                sql.append("ALTER TABLE ").append(tableName).append(" CHANGE ").append(oldName).append(" ");
                columnDefinition(sql, column, true);
                
                debug.string("alter", sql);
                
                Statement statement = connection.createStatement();
                statement.execute(sql.toString());
                statement.close();
            }
        }
        finally
        {
            debug.send();
        }
        
    }
}