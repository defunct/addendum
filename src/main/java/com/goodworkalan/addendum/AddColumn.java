package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that adds columns to an existing
 * table.
 * 
 * @author Alan Gutierrez
 */
public class AddColumn extends DefineColumn<AlterTable, AddColumn>
{
    /**
     * Add a new column to the table named by the given table name with the
     * given name and given column type.
     * 
     * @param table
     *            The table language element.
     * @param column
     *            The column object.
     */
    AddColumn(AlterTable table, Column column)
    {
        super(table, column);
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected AddColumn getElement()
    {
        return this;
    }

    /**
     * Set the column not null and initialize all existing values with the given
     * default value.
     * 
     * @param defaultValue
     *            The default not null value.
     * @return This column builder to continue construction.
     */
    public AddColumn notNull(String defaultValue)
    {
        column.setNotNull(true);
        column.setDefaultValue(defaultValue);
        return this;
    }
}
