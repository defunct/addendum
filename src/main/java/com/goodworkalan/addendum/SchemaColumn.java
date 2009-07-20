package com.goodworkalan.addendum;

/**
 * Specifies the property values to verify against an existing column in a table
 * in a table verification performed after all addenda are applied.
 * 
 * @author Alan Gutierrez
 */
public class SchemaColumn extends ExistingColumn<SchemaTable, SchemaColumn>
{
    /**
     * Create a schema verify column element with the given schema table parent
     * element element and the given column definition.
     * 
     * @param table
     *            The assert table parent element.
     * @param column
     *            The column definition.
     */
    public SchemaColumn(SchemaTable table, Column column)
    {
        super(table, column);
    }

    /**
     * Return this schema column element to continue the verify column
     * statement.
     * 
     * @return This assert column element.
     */
    @Override
    protected SchemaColumn getElement()
    {
        return this;
    }
}
