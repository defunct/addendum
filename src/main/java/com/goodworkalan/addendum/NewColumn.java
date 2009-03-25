package com.goodworkalan.addendum;

/**
 * A column builder for columns in a new table in the domain-specific language
 * used by {@link DatabaseAddendeum} to define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class NewColumn extends Column<Table, NewColumn>
{
    /**
     * Create a new column in the given table builder with the given name and
     * given column type.
     * 
     * @param table
     *            The table language element.
     * @param name
     *            The column name.
     * @param columnType
     *            Type column type.
     */
    public NewColumn(Table table, String name, ColumnType columnType)
    {
        super(table, name, columnType);
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

    /**
     * Set the unique id generator type;
     * 
     * @param generatorType
     *            The unique id generator type.
     * @return This column builder to continue construction.
     */
    public NewColumn generator(GeneratorType generatorType)
    {
        setGeneratorType(generatorType);
        return this;
    }
    
    /**
     * Set the column to not null.
     * 
     * @return This column builder to continue construction.
     */
    public NewColumn notNull()
    {
        setNotNull(true);
        return this;
    }

    /**
     * Set the default column value.
     * 
     * @param defaultValue
     *            The default column value.
     * @return This column builder to continue construction.
     */
    public NewColumn defaultValue(String defaultValue)
    {
        setDefaultValue(defaultValue);
        return this;
    }
}
