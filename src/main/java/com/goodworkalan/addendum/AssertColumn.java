package com.goodworkalan.addendum;

/**
 * Specifies the property values to verify against an existing column
 * in a table in a table assertion. 
 * 
 * @author Alan Gutierrez
 */
public class AssertColumn extends ExistingColumn<AssertTable, AssertColumn>
{
    /**
     * Create an assert column element with the given assert table parent
     * element element and the given column definition.
     * 
     * @param table
     *            The assert table parent element.
     * @param column
     *            The column definition.
     */
    public AssertColumn(AssertTable table, Column column)
    {
        super(table, column);
    }

    /**
     * Return this assert column element to continue the assert column
     * statement.
     * 
     * @return This assert column element.
     */
    @Override
    protected AssertColumn getElement()
    {
        return this;
    }
}
