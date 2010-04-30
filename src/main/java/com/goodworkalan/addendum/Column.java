package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.UNMAPPABLE_TYPE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import com.goodworkalan.utility.Primitives;

/**
 * A mutable object that models a column specification.
 * 
 * @author Alan Gutierrez
 */
public class Column {
    /** The name column name. */
    private String name;

    /** The <code>java.sql.Types</code> column type. */
    private int columnType;

    /** True if there is a default value. */
    private boolean hasDefaultValue;

    /** The default value. */
    private Object defaultValue;

    /** The not null flag. */
    private boolean notNull;

    /** The column length. */
    private Integer length;

    /** The column precision. */
    private Integer precision;

    /** The column scale. */
    private Integer scale;

    /** The unique id generator type. */
    private GeneratorType generatorType;

    public Column(Column column) {
        this.name = column.name;
        this.columnType = column.columnType;
        this.hasDefaultValue = column.hasDefaultValue;
        this.defaultValue = column.defaultValue;
        this.notNull = column.notNull;
        this.length = column.length;
        this.precision = column.precision;
        this.scale = column.scale;
        this.generatorType = column.generatorType;
    }

    /**
     * Create a column with the given name and the given
     * <code>java.sql.Types</code> type with the column properties set to their
     * default values.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The <code>java.sql.Types</code> column type.
     */
    public Column(String name, int columnType) {
        setName(name);
        setColumnType(columnType);
    }

    /**
     * Create a column with the given name and a <code>java.sql.Types</code>
     * column type appropriate for the given native Java type with the column
     * properties set to their default values.
     * 
     * @param name
     *            The column name.
     * @param nativeType
     *            The native Java type.
     */
    public Column(String name, Class<?> nativeType) {
        this(name, getColumnType(nativeType));
    }

    /**
     * Create a column with the given name. All other column properties are set
     * to an undefined state.
     * 
     * @param name
     *            The column name.
     */
    public Column(String name) {
        setName(name);
    }

    /**
     * Return a <code>java.sql.Types</code> type appropriate for the given
     * native type.
     * 
     * @param nativeType
     *            The Java native type.
     * @return An SQL type that can store the given native type.
     */
    public static int getColumnType(Class<?> nativeType) {
        nativeType = Primitives.box(nativeType);
        if (nativeType.equals(Boolean.class)) {
            return Types.BIT;
        } else if (nativeType.equals(Short.class)) {
            return Types.TINYINT;
        } else if (nativeType.equals(Character.class)) {
            return Types.SMALLINT;
        } else if (nativeType.equals(Integer.class)) {
            return Types.INTEGER;
        } else if (nativeType.equals(Long.class)) {
            return Types.BIGINT;
        } else if (nativeType.equals(Float.class)) {
            return Types.FLOAT;
        } else if (nativeType.equals(Double.class)) {
            return Types.DOUBLE;
        } else if (BigDecimal.class.isAssignableFrom(nativeType)) {
            return Types.NUMERIC;
        } else if (BigInteger.class.isAssignableFrom(nativeType)) {
            return Types.NUMERIC;
        } else if (Date.class.isAssignableFrom(nativeType)) {
            return Types.TIMESTAMP;
        } else if (Calendar.class.isAssignableFrom(nativeType)) {
            return Types.TIMESTAMP;
        } else if (String.class.isAssignableFrom(nativeType)) {
            return Types.VARCHAR;
        }
        throw new AddendumException(UNMAPPABLE_TYPE, nativeType.getCanonicalName());
    }

    /**
     * Get the column name.
     * 
     * @return The column name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the column name.
     * 
     * @param name
     *            The column name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the <code>java.sql.Types</code> column type.
     * 
     * @return The <code>java.sql.Types</code> column type.
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * Set the <code>java.sql.Types</code> column type.
     * 
     * @param columnType
     *            The <code>java.sql.Types</code> column type.
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
     * Set the column type to a <code>java.sql.Types</code> column type
     * appropriate for the given native Java type.
     * 
     * @param nativeType
     *            The native Java type.
     */
    public void setColumnType(Class<?> nativeType) {
        setColumnType(getColumnType(nativeType));
    }

    /**
     * Get the not null flag.
     * 
     * @return The not null flag.
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * Set the not null flag.
     * 
     * @param notNull
     *            The not null flag.
     */
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    /**
     * Get the column length.
     * 
     * @return The column length.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Set the column length.
     * 
     * @param length
     *            The column length.
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Get the column precision.
     * 
     * @return The column precision.
     */
    public Integer getPrecision() {
        return precision;
    }

    /**
     * Set the column precision.
     * 
     * @param precision
     *            The column precision.
     */
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    /**
     * Get the column scale.
     * 
     * @return The column scale.
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Set the column scale.
     * 
     * @param scale
     *            The column scale.
     */
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    /**
     * Get the default value.
     * 
     * @return The default value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Set the default value.
     * 
     * @param defaultValue
     *            The default value.
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Get the generator type.
     * 
     * @return The generator type.
     */
    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    /**
     * Set the generator type.
     * 
     * @param generatorType
     *            The generator type.
     */
    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }
}
