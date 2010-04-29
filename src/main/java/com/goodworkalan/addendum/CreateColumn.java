package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that specifies the properties of a
 * column in a create table statement.
 * 
 * 
 * @author Alan Gutierrez
 */
public class CreateColumn extends FreshColumn<DefineEntity, CreateColumn>
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
    public CreateColumn(DefineEntity table, Column column)
    {
        super(table, column);
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected CreateColumn getElement()
    {
        return this;
    }
}
