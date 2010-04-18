package com.goodworkalan.addendum;

/**
 * Specifies the property values to verify against an existing column in a table
 * in a table assertion.
 * <p>
 * FIXME Who cares? If you fall apart during a migration, you're cause is lost.
 * Make sure the output is correct before running it in production. There are
 * many ways to do this. I'm not sure that this migration library is the right
 * way. It makes the library so much more complex.
 * 
 * @author Alan Gutierrez
 */
public class VerifyColumn extends ExistingColumn<VerifyTable, VerifyColumn>
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
    public VerifyColumn(VerifyTable table, Column column)
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
    protected VerifyColumn getElement()
    {
        return this;
    }
}
