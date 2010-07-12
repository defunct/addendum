package com.goodworkalan.addendum.dialect;

import static com.goodworkalan.addendum.AddendumException.DIALECT_DOES_NOT_SUPPORT_GENERATOR;
import static com.goodworkalan.addendum.AddendumException.DIALECT_DOES_NOT_SUPPORT_TYPE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.goodworkalan.addendum.AddendumException;
import com.goodworkalan.notice.Notice;
import com.goodworkalan.notice.NoticeFactory;

/**
 * An abstract dialect with default implementations of create table and insert
 * using standard SQL.
 * 
 * @author Alan Gutierrez
 */
public abstract class AbstractDialect implements Dialect {
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, SortedMap<Integer, String>> typeNames;
    
    /** A map of SQL type flags to their SQL type names for the dialect. */
    private final Map<Integer, int[]> defaultPrecisionScale;
    
    /** A map of default lengths. */
    private final Map<Integer, Integer> defaultLengths;

    /** Create a base dialect. */
    protected AbstractDialect() {
        this.typeNames = new HashMap<Integer, SortedMap<Integer, String>>();
        this.defaultPrecisionScale = new HashMap<Integer, int[]>();
        this.defaultLengths = new HashMap<Integer, Integer>();
    }

    /**
     * Get the logger used to log messages from the dialect.
     * 
     * @return The logger.
     */
    protected abstract NoticeFactory getNoticeFactory();

    /**
     * Maps the given SQL type to the given SQL type name in the dialect.
     * 
     * @param type
     *            The SQL type.
     * @param name
     *            The SQL type name.
     */
    protected void setType(int type, String name) {
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
    protected void setType(int type, String name, int maxLength) {
        if (!typeNames.containsKey(type)) {
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
    protected void setDefaultPrecisionScale(int type, int precision, int scale) {
        defaultPrecisionScale.put(type, new int[] { precision, scale });
    }

    /**
     * Set the default length for the given SQL type to the given length.
     * 
     * @param type
     *            The SQL type.
     * @param length
     *            The default length.
     */
    protected void setDefaultLength(int type, int length) {
        defaultLengths.put(type, length);
    }

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
    public void createTable(Connection connection, String tableName, Collection<Column> columns, List<String> primaryKey) throws SQLException {
        Notice info = getNoticeFactory().info("create");
        try {
            info.put("columns", columns);
       
            StringBuilder sql = new StringBuilder();
            
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            String separator = "";
            for (Column column : columns) {
                sql.append(separator);
                columnDefinition(sql, column, true);
                separator = ",\n";
            }
            
            if (!primaryKey.isEmpty()) {
                sql.append(separator);
                sql.append("PRIMARY KEY (");
                String keySeparator = "";
                for (String key : primaryKey) {
                    sql.append(keySeparator).append(key);
                    keySeparator = ", ";
                }
                sql.append(")");
            }
    
            sql.append("\n)");
            
            info.put("sql", sql);
            
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            statement.close();
        } finally {
            info.send();
        }
    }

    /**
     * Cobertura has a bug where each branch of an enum switch statement must
     * execute a line of code for it to be counted. I've put this in place to
     * remind myself why I've done this. It is not just a compulsion, I do want
     * to be able to see that all of the branches are covered.
     */
    private void coberturaFallThrough() {
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
    protected void columnDefinition(StringBuilder sql, Column column, boolean canNull) {
        sql.append(column.getName()).append(" ");
        String pattern = null;
        
        Integer length = column.getLength();
        if (length == null) {
            length = defaultLengths.get(column.getColumnType());
        }
        if (length == null) {
            length = 0;
        }
        
        int[] precisionScale = defaultPrecisionScale.get(column.getColumnType());
        if (precisionScale != null) {
            if (column.getPrecision() == null) {
                column.setPrecision(precisionScale[0]);
            }
            if (column.getScale() == null) {
                column.setScale(precisionScale[1]);
            }
        }
        
        if (!typeNames.containsKey(column.getColumnType())) {
            throw new AddendumException(DIALECT_DOES_NOT_SUPPORT_TYPE, column.getColumnType());
        }

        for (Map.Entry<Integer, String> name : typeNames.get(column.getColumnType()).entrySet()) {
            if (length > name.getKey()) {
                continue;
            } else if (pattern != null) {
                break;
            }
            pattern = name.getValue();
        }
        
        sql.append(String.format(pattern, length, column.getPrecision(), column.getScale()));
        
        if (canNull && column.isNotNull()) {
            sql.append(" NOT NULL");
        }

        if (column.getGeneratorType() != null) {
            switch (column.getGeneratorType()) {
            case NONE:
                break;
            case IDENTITY:
                coberturaFallThrough();
            case AUTO:
                sql.append(" AUTO_INCREMENT");
                break;
            default:
                throw new AddendumException(DIALECT_DOES_NOT_SUPPORT_GENERATOR, column.getGeneratorType().toString());
            }
        }
        
        if (column.getDefaultValue() != null) {
            switch (column.getColumnType()) {
            case Types.CHAR:
                coberturaFallThrough();
            case Types.VARCHAR:
                coberturaFallThrough();
            case Types.DATE:
                coberturaFallThrough();
            case Types.TIME:
                coberturaFallThrough();
            case Types.TIMESTAMP:
                sql.append(" DEFAULT ").append("'" + column.getDefaultValue().toString().replace("'", "''") + "'");
                break;
            default:
                sql.append(" DEFAULT ").append(column.getDefaultValue().toString());
                break;
            }
        }
    }

    /**
     * Add a the given column definition to the the given table.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void addColumn(Connection connection, String tableName, Column column) throws SQLException {
        Notice info = getNoticeFactory().info("add.column");
        
        info.put("tableName", tableName).put("column", column);
        
        StringBuilder addSql = new StringBuilder();
        addSql.append("ALTER TABLE ").append(tableName).append(" ADD ");
        columnDefinition(addSql, column, false);
        
        info.put("add", addSql);
        
        Statement statement = connection.createStatement();
        statement.execute(addSql.toString());
        statement.close();
        
        if (column.isNotNull()) {
            StringBuilder updateSql = new StringBuilder();
            updateSql.append("UPDATE ").append(tableName)
                     .append(" SET ").append(column.getName()).append(" = ?");
            
            info
                .map("update")
                    .put("sql", updateSql)
                    .put("defaultValue", column.getDefaultValue())
                    .end();
            
            PreparedStatement prepared = connection.prepareStatement(updateSql.toString());
            prepared.setObject(1, column.getDefaultValue());
            prepared.execute();
            
            alterColumn(connection, tableName, column.getName(), column);
        }
        
        
        info.send();
    }

    /**
     * Drop the column with the given column name from the table with the given
     * table name using the given connection.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param columnName
     *            The column name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void dropColumn(Connection connection, String tableName, String columnName)
    throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("ALTER TABLE %s DROP COLUMN %s", tableName, columnName));
        statement.close();
    }

    /**
     * Verify that a table with the given table name exists in the database.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void verifyTable(Connection connection, String tableName, List<Column> columns) throws SQLException {
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
    public void insert(Connection connection, String table, List<String> columns, List<String> values) throws SQLException {
        Notice info = getNoticeFactory().info("insert");
        try {
            info.put("table", table).put("columns", columns).put("values", values);
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("INSERT INTO ").append(table).append("(");
            
            String separator = "";
            
            for (String column : columns) {
                sql.append(separator).append(column);
                separator = ", ";
            }
            
            sql.append(")\n");
            
            sql.append("VALUES(");
            
            separator = "";
            for (int i = 0; i < values.size(); i++) {
                sql.append(separator).append("?");
                separator = ", ";
            }
            
            sql.append(")\n");
            
            info.put("sql", sql);
            
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                if (value == null) {
                    statement.setNull(i + 1, Types.VARCHAR);
                } else {
                    statement.setString(i + 1, value);
                }
            }
            
            statement.execute();
            statement.close();
        } finally {
            info.send();
        }
    }
}
