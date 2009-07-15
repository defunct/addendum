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
     * @param name
     *            The  SQL column name.
     * @param columnType
     *            Type column type.
     */
    public NewColumn(NewTable table, String name, int columnType)
    {
        super(table, name, columnType);
    }

    /**
     * Create a new column in the given table builder with the given name and
     * column type for the given Java native type.
     * 
     * @param table
     *            The table language element.
     * @param name
     *            The SQL column name.
     * @param columnType
     *            Type column type.
     */
    public NewColumn(NewTable table, String name, Class<?> nativeType)
    {
        super(table, name, DefineColumn.getColumnType(nativeType));
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected NewColumn getBuilder()
    {
        return this;
    }
}
