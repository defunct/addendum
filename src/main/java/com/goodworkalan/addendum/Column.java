package com.goodworkalan.addendum;

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
public abstract class Column<Container, Builder>
{
    /** The containing domain-specific language element. */
    private Container container;
    
    /** The name of the column. */
    private String name;
    
    /** The type of the column. */
    private ColumnType columnType;

    /** The default value. */
    private String defaultValue;
    
    /** The not null flag. */
    private boolean notNull;
    
    /** The column length. */
    private int length;

    /** The column precision. */
    private int precision;
    
    /** The unique id generator type. */
    private GeneratorType generatorType;

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
    Column(Container container, String name, ColumnType columnType)
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
     * Get the column type.
     * 
     * @return The column type.
     */
    public ColumnType getColumnType()
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
