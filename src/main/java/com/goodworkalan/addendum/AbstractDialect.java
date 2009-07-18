package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
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

/**
 * An abstract dialect with default implementations of create table and insert
 * using standard SQL. The dialect includes methods for escaping SQL and
 * obtaining column metadata.
 * 
 * @author Alan Gutierrez
 */
public abstract class AbstractDialect implements Dialect
{
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, SortedMap<Integer, String>> typeNames;
    
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, int[]> defaultPrecisionScale;
    
    /**
     * Create a base dialect.
     */
    protected AbstractDialect()
    {
        this.typeNames = new HashMap<Integer, SortedMap<Integer, String>>();
        this.defaultPrecisionScale = new HashMap<Integer, int[]>();
    }

    /**
     * Get the logger used to log messages from the dialect.
     * 
     * @return The logger.
     */
    protected abstract Logger getLogger();

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
    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException
    {
        Log info = getLogger().info();
        try
        {
            info.message("Creating table %s.", tableName);
            
            info.bean("columns", columns);
            
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
            
            info.string("sql", sql);
            
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            statement.close();
        }
        finally
        {
            info.send();
        }
    }

    /**
     * Append the column definition SQL to the given string buffer. If the given
     * <code>canNull</code> parameter is false, than the <strong>
     * <code>NOT NULL</code></strong> modifier will not be added to the column
     * definition SQL even if the column not null property is true.
     * 
     * @param sql
     *            The string buffer to which the SQL is appended.
     * @param column
     *            The column definition.
     * @param canNull
     *            If false, <strong><code>NOT NULL</code></strong> modifier
     *            will not be added to the column definition SQL even if the
     *            column not null property is true.
     */
    protected void columnDefinition(StringBuilder sql, Column column, boolean canNull)
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

    /**
     * Perform a case transform on the given table name for use as a metadata
     * selection critieria.
     * 
     * @param tableName
     *            The table name.
     * @return The table name with the correct case for use as a metadata
     *         selection critieria.
     */
    protected abstract String tableCase(String tableName);

    /**
     * Perform a case transform on the given column name for use as a metadata
     * selection critieria.
     * 
     * @param columnName
     *            The table name.
     * @return The column name with the correct case for use as a metadata
     *         selection critieria.
     */
    protected abstract String columnCase(String columnName);

    /**
     * Create a column contianing the column metadata for the given column in
     * the given table.
     * 
     * @param connection
     *            The JDBC conneciton.
     * @param tableName
     *            The table name.
     * @param columnName
     *            The column name.
     * @return A column containing the column metadata.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    protected Column getMetaColumn(Connection connection, String tableName, String columnName) throws SQLException
    {
        Column column = new Column(columnName);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT " + columnCase(column.getName()) + " FROM " + tableCase(tableName));
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
        
        rs = connection.getMetaData().getColumns(null, null, tableCase(tableName), columnCase(columnName));
        if (!rs.next())
        {
            throw new RuntimeException("Table: " + tableName + ", column: " + columnName);
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

    /**
     * Add a the given column definition to the the given table.
     * 
     * @param connection
     *            The JDBC conneciton.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException
    {
        Log info = getLogger().info();
        
        info.message("Adding column %s to table %s.", column.getName(), tableName);
        
        info.string("tableName", tableName).bean("column", column);
        
        StringBuilder addSql = new StringBuilder();
        addSql.append("ALTER TABLE ").append(tableName).append(" ADD ");
        columnDefinition(addSql, column, false);
        
        info.string("add", addSql);
        
        Statement statement = connection.createStatement();
        statement.execute(addSql.toString());
        
        if (column.isNotNull())
        {
            StringBuilder updateSql = new StringBuilder();
            updateSql.append("UPDATE ").append(tableName)
                     .append(" SET ").append(column.getName()).append(" = ?");
            
            info
                .map("update")
                    .string("sql", updateSql)
                    .string("defaultValue", column.getDefaultValue())
                    .end();
            
            PreparedStatement prepared = connection.prepareStatement(updateSql.toString());
            prepared.setString(1, column.getDefaultValue());
            prepared.execute();
            
            StringBuilder alterSql = new StringBuilder();
            alterSql.append("ALTER TABLE ").append(tableName)
                    .append(" CHANGE ").append(column.getName()).append(" ").append(column.getName()).append(" ");
            columnDefinition(alterSql, column, true);
            
            info.string("alter", alterSql);
            
            statement.execute(alterSql.toString());
        }
        
        statement.close();
        
        info.send();
    }

    /**
     * Inhert the properties of the given full column by assigning them to
     * unspecified values in the partial column.
     * 
     * @param partial
     *            The partial column that inherits.
     * @param full
     *            The full column to inherit from.
     */
    protected void inherit(Column partial, Column full)
    {
        if (partial.getColumnType() == null)
        {
            partial.setColumnType(full.getColumnType());
        }
        if (partial.getLength() == null)
        {
            partial.setLength(full.getLength());
        }
        if (partial.getPrecision() == null)
        {
            partial.setPrecision(full.getPrecision());
        }
        if (partial.getScale() == null)
        {
            partial.setScale(full.getScale());
        }
        if (partial.getDefaultValue() == null)
        {
            partial.setDefaultValue(full.getDefaultValue());
        }
        if (partial.isNotNull() == null)
        {
            partial.setNotNull(full.isNotNull());
        }
        if (partial.getGeneratorType() == null)
        {
            partial.setGeneratorType(full.getGeneratorType());
        }
    }

    /**
     * Verify that a column in the given table has the given column definition.
     * 
     * @param connection
     *            The JDBC conneciton.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void verifyColumn(Connection connection, String tableName, Column column) throws SQLException
    {
    }

    /**
     * Rename a table form the given old name to the given new name.
     * 
     * @param connection
     *            The JDBC conneciton.
     * @param oldName
     *            The old table name.
     * @param newName
     *            The new table name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void renameTable(Connection connection, String oldName, String newName) throws SQLException
    {
        Log info = getLogger().info();
        
        info.message("Renaming table %s to %s.", oldName, newName);
        info.string("oldName", oldName).string("newName", newName);

        StringBuilder sql = new StringBuilder();
        sql.append("RENAME TABLE ").append(oldName).append(" TO ").append(newName);
        
        info.string("sql", sql);
        
        info.send();

        Statement statement = connection.createStatement();
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
    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException
    {
        Log info = getLogger().info();
        try
        {
            info.message("Inserting record into %s.", table);
            
            info.string("table", table).bean("columns", columns).bean("values", values);
            
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
            
            info.string("sql", sql);
            
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
        finally
        {
            info.send();
        }
    }
}
