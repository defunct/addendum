package com.goodworkalan.addendum;


/**
 * A column builder for columns in a new table in the domain-specific language
 * used by {@link DatabaseAddendeum} to define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class NewColumn extends FreshColumn<NewTable, NewColumn>
{
    /**
     * Create a new column in the given table builder with the given name and
     * given column type.
     * 
     * @param table
     *            The table language element.
     * @param column
     *            The column.
     */
    public NewColumn(NewTable table, Column column)
    {
        super(table, column);
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected NewColumn getElement()
    {
        return this;
    }
}
