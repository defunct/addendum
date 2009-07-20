package com.goodworkalan.addendum;

/**
 * A generic column specification language element in the domain-specific
 * language used to define database update actions. There are separate
 * subclasses for column definitions for a new table, for adding columns to
 * an existing table, and for altering existing columns.
 * 
 * @param <Container>
 *            The type of the containing domain-specific language element.
 * @param <Element>
 *            The type of the subclassed column specification language element.
 * 
 * @author Alan Gutierrez
 */
public abstract class DefineColumn<Container, Element>
{
    /** The containing domain-specific language element. */
    private Container container;
    
    /** The column definition bean. */
    protected final Column column;

    /**
     * Create a column specifier with the given name and given column type.
     * 
     * @param container
     *            The containing language element.
     * @param column
     *            The column definition bean.
     */
    DefineColumn(Container container, Column column)
    {
        this.container = container;
        this.column = column;
    }

    /**
     * Return the column specification language element to continue the
     * domain-specific language statement.
     * 
     * @return The column specification language element.
     */
    protected abstract Element getElement();

    /**
     * Set the column length to the given length.
     * 
     * @param length The column length.
     */
    public Element length(int length)
    {
        column.setLength(length);
        return getElement();
    }

    /**
     * Set the column precision to the given precision.
     * 
     * @param precision
     *            The column precision.
     */
    public Element precision(int precision)
    {
        column.setPrecision(precision);
        return getElement();
    }
    
    /**
     * Set the column scale to the given precision.
     * 
     * @param scale
     *            The column scale.
     */
    public Element scale(int scale)
    {
        column.setScale(scale);
        return getElement();
    }

    /**
     * Set the default column value.
     * 
     * @param defaultValue
     *            The default column value.
     * @return This column builder to continue construction.
     */
    public Element defaultValue(Object defaultValue)
    {
        column.setDefaultValue(defaultValue);
        return getElement();
    }

    /**
     * Terminate the column definition and return the containing domain-specific
     * language element.
     * 
     * @return The table builder.
     */
    public Container end()
    {
        return container;
    }
}
