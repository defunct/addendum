package com.goodworkalan.addendum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

public class Column
{
    /** The name of the column. */
    private String name;
    
    /** The SQL type of the column. */
    private Integer columnType;

    /** The default value. */
    private String defaultValue;
    
    /** The not null flag. */
    private Boolean notNull;
    
    /** The column length. */
    private Integer length;

    /** The column precision. */
    private Integer precision;
    
    /** The column scale. */
    private Integer scale;
    
    /** The unique id generator type. */
    private GeneratorType generatorType;

    public Column(String name, int columnType)
    {
        setName(name);
        setColumnType(columnType);
    }
    
    public Column(String name, Class<?> columnType)
    {
        this(name, getColumnType(columnType));
    }

    public Column(String name)
    {
        setName(name);
    }

    public void setDefaults(int columnType)
    {
        setColumnType(columnType);
        setNotNull(false);
        setLength(0);
        setPrecision(0);
        setScale(0);
    }

    public void setDefaults(Class<?> nativeType)
    {    
        setDefaults(Column.getColumnType(nativeType));
    }
    
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
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Integer getColumnType()
    {
        return columnType;
    }
    
    public void setColumnType(Integer columnType)
    {
        this.columnType = columnType;
    }
    
    public void setColumnType(Class<?> nativeType)
    {
        setColumnType(getColumnType(nativeType));
    }
    
    public Boolean isNotNull()
    {
        return notNull;
    }
    
    public void setNotNull(Boolean notNull)
    {
        this.notNull = notNull;
    }
    
    public Integer getLength()
    {
        return length;
    }
    
    public void setLength(Integer length)
    {
        this.length = length;
    }
    
    // FIXME Merge with length.
    public Integer getPrecision()
    {
        return precision;
    }
    
    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }
    
    public Integer getScale()
    {
        return scale;
    }
    
    public void setScale(Integer scale)
    {
        this.scale = scale;
    }
    
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    public GeneratorType getGeneratorType()
    {
        return generatorType;
    }
    
    public void setGeneratorType(GeneratorType generatorType)
    {
        this.generatorType = generatorType;
    }
}
