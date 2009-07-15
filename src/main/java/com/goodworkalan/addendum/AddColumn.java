package com.goodworkalan.addendum;

/**
 * A column builder that adds columns to an existing table in the
 * domain-specific language used by {@link DatabaseAddendeum} to define database
 * update actions.
 * 
 * @author Alan Gutierrez
 */
public class AddColumn extends DefineColumn<Table, AddColumn>
{
    /**
     * Add a new column to the table named by the given table name with the
     * given name and given column type.
     * 
     * @param table
     *            The table language element.
     * @param tableName
     *            The name of the table in which to add the new column.
     * @param name
     *            The column name.
     * @param columnType
     *            Type SQL column type.
     */
    AddColumn(Table table, String name, int columnType)
    {
        super(table, name, columnType);
    }
    
    AddColumn(Table schema, String name, Class<?> nativeType)
    {
        this(schema, name, DefineColumn.getColumnType(nativeType));
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return The builder.
     */
    @Override
    protected AddColumn getBuilder()
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
        setNotNull(true);
        setDefaultValue(defaultValue);
        return this;
    }
}
