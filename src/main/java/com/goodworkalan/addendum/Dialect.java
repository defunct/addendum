package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.goodworkalan.prattle.Log;
import com.goodworkalan.prattle.Logger;
import com.goodworkalan.prattle.LoggerFactory;

/**
 * An implementation of {@link DatabaseAddendeum} database update actions for a
 * specific SQL dialect.
 * 
 * @author Alan Gutierrez
 */
// FIXME Make interface and then make AbstractDialect.
public abstract class Dialect
{
    private final static Logger logger = LoggerFactory.getLogger(Dialect.class)
    ;
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
        if (!typeNames.containsKey(type))
        {
            typeNames.put(type, new TreeMap<Integer, String>());
            setType(type, name, maxLength);
        }
        typeNames.get(type).put(maxLength, name);
    }

    /**
     * Sets the default precision and scale for the given SQL type to the given
     * precision and the given scale.
     * 
     * @param type
     *            The SQL type.
     * @param precision
     *            The default precision.
     * @param scale
     *            The default scale.
     */
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
    public abstract void createAddendaTable(Connection connection) throws SQLException;
    
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
    public String createTable(String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");
        
        String separator = "";
        for (Column column : columns)
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
                    continue;
                }
                else if (pattern != null)
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
            
            if (column.getGeneratorType() != null)
            {
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
    
    public void columnDefinition(StringBuilder sql, Column column, boolean canNull)
    {
        sql.append(column.getName()).append(" ");
        String pattern = null;
        
        int length = column.getLength();
        
        if (!typeNames.containsKey(column.getColumnType()))
        {
            throw new AddendumException(AddendumException.DIALECT_DOES_NOT_SUPPORT_TYPE);
        }

        for (Map.Entry<Integer, String> name : typeNames.get(column.getColumnType()).entrySet())
        {
            if (length > name.getKey())
            {
                continue;
            }
            else if (pattern != null)
            {
                break;
            }
            pattern = name.getValue();
        }
        
        sql.append(String.format(pattern, length, column.getPrecision(), column.getScale()));
        
        if (canNull && column.isNotNull())
        {
            sql.append(" NOT NULL");
        }
        
        if (column.getGeneratorType() != null)
        {
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
        }
        
        if (column.getDefaultValue() != null)
        {
            // FIXME Escape string values.
            sql.append(" DEFAULT ").append(column.getDefaultValue());
        }
    }
    
    protected Column getMetaColumn(Connection connection, String tableName, String columnName) throws SQLException
    {
        Column column = new Column(columnName);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT " + column.getName() + " FROM " + tableName);
        ResultSetMetaData meta = rs.getMetaData();
        if (meta.isAutoIncrement(1))
        {
            column.setGeneratorType(GeneratorType.AUTO_INCREMENT);
        }
        column.setColumnType(meta.getColumnType(1));
        column.setPrecision(meta.getPrecision(1));
        column.setScale(meta.getScale(1));
        rs.close();
        statement.close();
        
        rs = connection.getMetaData().getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase());
        if (!rs.next())
        {
            throw new AddendumException(0);
        }
        column.setNotNull(rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
        column.setDefaultValue(rs.getString("COLUMN_DEF"));
        switch (column.getColumnType())
        {
        case Types.CHAR:
        case Types.VARCHAR:
            column.setLength(rs.getInt("COLUMN_SIZE"));
        }
        return column;
    }
    
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException
    {
        Log debug = logger.debug();
        
        debug.message("Altering column %s in table %s.", column.getName(), tableName);
        
        debug.bean("tableName", tableName).bean("oldName", oldName).bean("column", column);
        
        if (!oldName.equals(column.getName()))
        {
            String sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + oldName + " RENAME TO " + column.getName();
            
            debug.bean("rename", sql);
            
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
                       || column.getDefaultValue() != null;

        if (altered)
        {
            Column meta = getMetaColumn(connection, tableName, column.getName());
            
            debug.bean("meta", meta);
            
            if (column.getColumnType() == null)
            {
                column.setColumnType(meta.getColumnType());
            }
            if (column.getLength() == null)
            {
                column.setLength(meta.getLength());
            }
            if (column.getPrecision() == null)
            {
                column.setPrecision(meta.getPrecision());
            }
            if (column.getScale() == null)
            {
                column.setScale(meta.getScale());
            }
            if (column.getDefaultValue() == null)
            {
                column.setDefaultValue(column.getDefaultValue());
            }
            if (column.isNotNull() == null)
            {
                column.setNotNull(column.isNotNull());
            }
            if (column.getGeneratorType() == null)
            {
                column.setGeneratorType(column.getGeneratorType());
            }

            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(tableName).append(" CHANGE ").append(oldName).append(" ");
            columnDefinition(sql, column, true);
            
            debug.bean("alter", sql);
            
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            statement.close();
        }
        
        debug.send();
    }
    
    
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException
    {
        Statement statement = connection.createStatement();
        
        StringBuilder addSql = new StringBuilder();
        addSql.append("ALTER TABLE ").append(tableName).append(" ADD ");
        columnDefinition(addSql, column, false);
        
        statement.execute(addSql.toString());
        
        if (column.isNotNull())
        {
            StringBuilder updateSql = new StringBuilder();
            updateSql.append("UPDATE ").append(tableName).append(" SET ").append(column.getName()).append(" = ").append(column.getDefaultValue()).append(" ");
            
            statement.execute(updateSql.toString());
            
            StringBuilder alterSql = new StringBuilder();
            alterSql.append("ALTER TABLE ").append(tableName).append(" CHANGE ").append(column.getName()).append(" ").append(column.getName()).append(" ");
            columnDefinition(alterSql, column, true);
            
            statement.execute(alterSql.toString());
        }
    }
    
    public void verifyColumn(Connection connection, String tableName, Column column) throws SQLException
    {
    }

    public void renameTable(Connection connection, String oldName, String newName) throws SQLException
    {
        Statement statement = connection.createStatement();
        StringBuilder sql = new StringBuilder();
        sql.append("RENAME TABLE ").append(oldName).append(" TO ").append(newName);
        statement.execute(sql.toString());
        statement.close();
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
