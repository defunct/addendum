package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An implementation of {@link DatabaseAddendeum} database update actions for a
 * specific SQL dialect.
 * 
 * @author Alan Gutierrez
 */
public abstract class Dialect
{
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, SortedMap<Integer, String>> typeNames;
    
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, int[]> defaultPrecisionScale;
    
    /**
     * Create a base dialect.
     */
    protected Dialect()
    {
        this.typeNames = new HashMap<Integer, SortedMap<Integer, String>>();
        this.defaultPrecisionScale = new HashMap<Integer, int[]>();
    }

    /**
     * Maps the given SQL type to the given SQL type name in the dialect.
     * 
     * @param type
     *            The SQL type.
     * @param name
     *            The SQL type name.
     */
    protected void setType(int type, String name)
    {
        setType(type, name, Integer.MAX_VALUE);
    }

    /**
     * Maps the given SQL type to the given SQL type name for lengths less than
     * or equal to the given max length in the dialect.
     * 
     * @param type
     *            The SQL type.
     * @param name
     *            The SQL type name.
     * @param maxLength
     *            The max length for which the name applies.
     */
    protected void setType(int type, String name, int maxLength)
    {
        if (!typeNames.containsKey(name))
        {
            typeNames.put(type, new TreeMap<Integer, String>());
            setType(type, name, maxLength);
        }
        typeNames.get(type).put(maxLength, name);
    }
    
    protected void setDefaultPrecisionScale(int type, int precision, int scale)
    {
        defaultPrecisionScale.put(type, new int[] { precision, scale });
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
    public abstract void createAddenda(Connection connection) throws SQLException;
    
    /**
     * Get the maximum update applied.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @return The maximum update applied.
     * @throws SQLException
     *             For any SQL error.
     */
    public abstract int addendaCount(Connection connection) throws SQLException;

    /**
     * Increment the update number in the database.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public abstract void addendum(Connection connection) throws SQLException;
    
    /**
     * Determine if the dialect can translate for the given connection.
     * 
     * @param connection
     *            The database connection.
     * @return True if this dialect can translate for the given connection.
     * @throws SQLException
     *             For any SQL error.
     */
    public abstract boolean canTranslate(Connection connection) throws SQLException;

    /**
     * Create a table with the given table name, given columns and the given
     * primary key fields.
     * 
     * @param tableName
     *            The table name.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The list of primary key fields.
     */
    public String createTable(String tableName, List<Column<?, ?>> columns, List<String> primaryKey) throws SQLException
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");
        
        String separator = "";
        for (Column<?, ?> column : columns)
        {
            sql.append(separator).append(column.getName()).append(" ");
        
            int length = column.getLength();
            if (!typeNames.containsKey(column.getColumnType()))
            {
                throw new AddendumException(AddendumException.DIALECT_DOES_NOT_SUPPORT_TYPE);
            }

            String pattern = null;
            for (Map.Entry<Integer, String> name : typeNames.get(column.getColumnType()).entrySet())
            {
                if (length > name.getKey())
                {
                    break;
                }
                pattern = name.getValue();
            }
            
            sql.append(String.format(pattern, length, column.getPrecision(), column.getScale()));
            
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
        
        return sql.toString();
    }

    /**
     * Insert a row into the given table. The column names are specified by the
     * given columns list. The values are specified by the given values list.
     * 
     * @param connection
     *            The database connection.
     * @param table
     *            The name of the table to insert into.
     * @param columns
     *            The name of insert columns.
     * @param values
     *            A parallel list of insert values, parallel to the insert
     *            columns.
     * @throws SQLException
     *             For any SQL error.
     */
    public String insert(String table, List<String> columns, List<String> values) throws SQLException
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
        
        sql.append(")\n");
        
        return sql.toString();
    }
}
