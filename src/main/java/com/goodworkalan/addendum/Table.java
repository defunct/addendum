package com.goodworkalan.addendum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A table in the domain-specific language used by {@link DatabaseAddendum} to
 * define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class Table
{
    /** The root language element. */
    private final Schema schema;

    /** The list of column definitions. */
    private final List<Column<?, ?>> columns;
    
    /** The primary key columns of the table. */
    private final List<String> primaryKey;

    /**
     * Create a table builder with the given root language element.
     * 
     * @param schema
     *            The root language element.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The primary key columns.
     */
    Table(Schema schema, String name, List<Column<?, ?>> columns, List<String> primaryKey)
    {
        this.schema = schema;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    /**
     * Define a new column in the table with the given name and given column
     * type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The SQL column type.
     * @return A column builder.
     */
    public NewColumn column(String name, int columnType)
    {
        NewColumn newColumn =  new NewColumn(this, name, columnType);
        columns.add(newColumn);
        return newColumn;
    }

    /**
     * Define a new column in the table with the given name and a column type
     * appropriate for the given Java primitive.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The native column type.
     * @return A column builder.
     */
    public NewColumn column(String name, Class<?> nativeType)
    {
        return column(name, getColumnType(nativeType));
    }

    /**
     * Return an SQL type appropriate for the given native type.
     * 
     * @param nativeType
     *            The Java native type.
     * @return An SQL type that can store the given native type.
     */
    private static int getColumnType(Class<?> nativeType)
    {
        if (nativeType.equals(boolean.class) || nativeType.equals(Boolean.class))
        {
            return Types.BIT;
        }
        else if (nativeType.equals(short.class) || nativeType.equals(Short.class))
        {
            return Types.TINYINT;
        }
        else if (nativeType.equals(char.class) || nativeType.equals(Character.class))
        {
            return Types.SMALLINT;
        }
        else if (nativeType.equals(int.class) || nativeType.equals(Integer.class))
        {
            return Types.INTEGER;
        }
        else if (nativeType.equals(long.class) || nativeType.equals(Long.class))
        {
            return Types.BIGINT;
        }
        else if (nativeType.equals(float.class) || nativeType.equals(Float.class))
        {
            return Types.FLOAT;
        }
        else if (nativeType.equals(double.class) || nativeType.equals(Double.class))
        {
            return Types.DOUBLE;
        }
        else if (BigDecimal.class.isAssignableFrom(nativeType))
        {
            return Types.NUMERIC;
        }
        else if (BigInteger.class.isAssignableFrom(nativeType))
        {
            return Types.NUMERIC;
        }
        else if (Date.class.isAssignableFrom(nativeType))
        {
            return Types.TIMESTAMP;
        }
        else if (Calendar.class.isAssignableFrom(nativeType))
        {
            return Types.TIMESTAMP;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Define the primary key of the table.
     * 
     * @param columns
     *            The primary key column names.
     * @return This builder to continue building.
     */
    public Table primaryKey(String... columns)
    {
        primaryKey.addAll(Arrays.asList(columns));
        return this;
    }


    /**
     * Terminate the table definition and return the schema.
     * 
     * @return The schema.
     */
    public Schema end()
    {
        return schema;
    }
}