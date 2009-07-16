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
public abstract class DefineColumn<Container, Builder>
{
    /** The containing domain-specific language element. */
    private Container container;
    
    protected final Column column;
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
    DefineColumn(Container container, Column column)
    {
        this.container = container;
        this.column = column;
    }

    /**
     * Return the builder to continue construction.
     * 
     * @return The builder.
     */
    protected abstract Builder getBuilder();

    /**
     * Set the column length to the given length.
     * 
     * @param length The column length.
     */
    public Builder length(int length)
    {
        column.setLength(length);
        return getBuilder();
    }

    /**
     * Set the column precision to the given precision.
     * 
     * @param precision
     *            The column precision.
     */
    public Builder precision(int precision)
    {
        column.setPrecision(precision);
        return getBuilder();
    }
    
    /**
     * Set the column scale to the given precision.
     * 
     * @param precision
     *            The column precision.
     */
    public Builder scale(int scale)
    {
        column.setScale(scale);
        return getBuilder();
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
