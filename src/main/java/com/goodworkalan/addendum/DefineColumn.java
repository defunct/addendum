package com.goodworkalan.addendum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

/**
 * A generic column builder in the domain-specific language used by
 * {@link DatabaseAddendeum} to define database update actions. There are
 * separate subclasses for column definitions for a new table and for adding
 * columns to an existing table.
 * 
 * @param <Container>
 *            The type of the containing domain-specific language element.
 * @param <Builder>
 *            The type of the subclassed column builder.
 * 
 * @author Alan Gutierrez
 */
public abstract class DefineColumn<Container, Builder>
{
    /** The containing domain-specific language element. */
    private Container container;
    
    /** The name of the column. */
    private String name;
    
    /** The SQL type of the column. */
    private int columnType;

    /** The default value. */
    private String defaultValue;
    
    /** The not null flag. */
    private boolean notNull;
    
    /** The column length. */
    private int length;

    /** The column precision. */
    private int precision;
    
    /** The column scale. */
    private int scale;
    
    /** The unique id generator type. */
    private GeneratorType generatorType;

    /**
     * Return an SQL type appropriate for the given native type.
     * 
     * @param nativeType
     *            The Java native type.
     * @return An SQL type that can store the given native type.
     */
    public static int getColumnType(Class<?> nativeType)
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
     * Create a column with the given name and given column type.
     * 
     * @param container
     *            The contianing language element.
     * @param name
     *            The column name.
     * @param columnType
     *            The column type.
     */
    DefineColumn(Container container, String name, int columnType)
    {
        this.container = container;
        this.name = name;
        this.columnType = columnType;
        this.generatorType = GeneratorType.NONE;
    }

    /**
     * Return the builder to continue construction.
     * 
     * @return The builder.
     */
    protected abstract Builder getBuilder();

    /**
     * Get the column name.
     * 
     * @return The column name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the SQL column type.
     * 
     * @param columnType
     *            The SQL column type.
     */
    protected void setColumnType(int columnType)
    {
        this.columnType = columnType;
    }

    /**
     * Get the SQL column type.
     * 
     * @return The SQL column type.
     */
    public int getColumnType()
    {
        return columnType;
    }

    /**
     * Set the unique id generator type.
     * 
     * @param generatorType
     *            The unique id generator type.
     */
    protected void setGeneratorType(GeneratorType generatorType)
    {
        this.generatorType = generatorType;
    }

    /**
     * Get the unique id generator type.
     * 
     * @return The unique id generator type.
     */
    public GeneratorType getGeneratorType()
    {
        return generatorType;
    }

    /**
     * Set the column length to the given length.
     * 
     * @param length The column length.
     */
    public Builder length(int length)
    {
        this.length = length;
        return getBuilder();
    }

    /**
     * Get the column length.
     * 
     * @return The column length.
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Set the column precision to the given precision.
     * 
     * @param precision
     *            The column precision.
     */
    public Builder precision(int precision)
    {
        this.precision = precision;
        return getBuilder();
    }

    /**
     * Get the column precision.
     * 
     * @return The column precision.
     */
    public int getPrecision()
    {
        return precision;
    }
    
    /**
     * Set the column scale to the given precision.
     * 
     * @param precision
     *            The column precision.
     */
    public Builder scale(int scale)
    {
        this.scale = scale;
        return getBuilder();
    }
    
    
    /**
     * Get the column scale.
     * 
     * @return The column scale.
     */
    public int getScale()
    {
        return scale;
    }

    /**
     * Set to true to indicate that the column is not null.
     * 
     * @param notNull
     *            True to indicate not null.
     */
    protected void setNotNull(boolean notNull)
    {
        this.notNull = notNull;
    }

    /**
     * Get the not null state.
     * 
     * @return True if the colum is not null.
     */
    public boolean isNotNull()
    {
        return notNull;
    }

    /**
     * Set the default value of the column.
     * 
     * @param defaultValue
     *            The default value.
     */
    protected void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Get the default column value.
     * 
     * @return The default column value.
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    /**
     * Terminate the column definition and return the containing domain-specific langauge element.
     * 
     * @return The table builder.
     */
    public Container end()
    {
        return container;
    }
}
