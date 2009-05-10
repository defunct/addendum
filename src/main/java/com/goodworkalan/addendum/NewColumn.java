package com.goodworkalan.addendum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

/**
 * A column builder for columns in a new table in the domain-specific language
 * used by {@link DatabaseAddendeum} to define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class NewColumn extends Column<Table, NewColumn>
{
    /**
     * Create a new column in the given table builder with the given name and
     * given column type.
     * 
     * @param table
     *            The table language element.
     * @param name
     *            The  SQL column name.
     * @param columnType
     *            Type column type.
     */
    public NewColumn(Table table, String name, int columnType)
    {
        super(table, name, columnType);
    }

    /**
     * Create a new column in the given table builder with the given name and
     * column type for the given Java native type.
     * 
     * @param table
     *            The table language element.
     * @param name
     *            The SQL column name.
     * @param columnType
     *            Type column type.
     */
    public NewColumn(Table table, String name, Class<?> nativeType)
    {
        super(table, name, getColumnType(nativeType));
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
        else if (String.class.isAssignableFrom(nativeType))
        {
            return Types.VARCHAR;
        }
        throw new IllegalArgumentException();
    }


    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected NewColumn getBuilder()
    {
        return this;
    }

    /**
     * Set the unique id generator type;
     * 
     * @param generatorType
     *            The unique id generator type.
     * @return This column builder to continue construction.
     */
    public NewColumn generator(GeneratorType generatorType)
    {
        setGeneratorType(generatorType);
        return this;
    }
    
    /**
     * Set the column to not null.
     * 
     * @return This column builder to continue construction.
     */
    public NewColumn notNull()
    {
        setNotNull(true);
        return this;
    }

    /**
     * Set the default column value.
     * 
     * @param defaultValue
     *            The default column value.
     * @return This column builder to continue construction.
     */
    public NewColumn defaultValue(String defaultValue)
    {
        setDefaultValue(defaultValue);
        return this;
    }
}
