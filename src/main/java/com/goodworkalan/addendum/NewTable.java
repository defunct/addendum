package com.goodworkalan.addendum;

import java.util.Arrays;
import java.util.List;

/**
 * Begins a create table statement in the domain-specific language used to
 * define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class NewTable
{
    /** The root language element. */
    private final Schema schema;
    
    /** The table definition bean. */
    private final Table table;
    
    /** The primary key columns of the table. */
    private final List<String> primaryKey;

    /**
     * Create a table builder with the given root language element.
     * 
     * @param schema
     *            The root language element.
     * @param columns
     *            The list of column definitions.
     * @param primaryKey
     *            The primary key columns.
     */
    NewTable(Schema schema, Table table, List<String> primaryKey)
    {
        this.schema = schema;
        this.table = table;
        this.primaryKey = primaryKey;
    }
    
    public Column newColumn(String name)
    {
        if (table.getColumns().containsKey(name))
        {
            throw new AddendumException(0);
        }
        Column column = new Column(name);
        table.getColumns().put(name, column);
        return column;
    }

    /**
     * Define a new column in the table with the given name and given column
     * type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The SQL column type.
     * @return A column builder.
     */
    public NewColumn column(String name, int columnType)
    {
        Column column = newColumn(name);
        column.setDefaults(columnType);
        return new NewColumn(this, column);
    }

    /**
     * Define a new column in the table with the given name and a column type
     * appropriate for the given Java primitive.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The native column type.
     * @return A column builder.
     */
    public NewColumn column(String name, Class<?> nativeType)
    {
        Column column = newColumn(name);
        column.setDefaults(nativeType);
        return new NewColumn(this, column);
    }

    /**
     * Define the primary key of the table.
     * 
     * @param columns
     *            The primary key column names.
     * @return This builder to continue building.
     */
    public NewTable primaryKey(String... columns)
    {
        primaryKey.addAll(Arrays.asList(columns));
        return this;
    }


    /**
     * Terminate the table definition and return the schema.
     * 
     * @return The schema.
     */
    public Schema end()
    {
        return schema;
    }
}