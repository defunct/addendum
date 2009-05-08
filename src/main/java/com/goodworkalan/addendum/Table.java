package com.goodworkalan.addendum;

import java.util.Arrays;
import java.util.List;

/**
 * A table in the domain-specific language used by {@link DatabaseAddendum} to
 * define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class Table
{
    /** The root language element. */
    private final Schema schema;

    /** The list of column definitions. */
    private final List<Column<?, ?>> columns;
    
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
    Table(Schema schema, String name, List<Column<?, ?>> columns, List<String> primaryKey)
    {
        this.schema = schema;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    /**
     * Define a new column in the table with the given name and given column
     * type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The column type.
     * @return A column builder.
     */
    public NewColumn column(String name, ColumnType columnType)
    {
        NewColumn newColumn =  new NewColumn(this, name, columnType);
        columns.add(newColumn);
        return newColumn;
    }

    /**
     * Define the primary key of the table.
     * 
     * @param columns
     *            The primary key column names.
     * @return This builder to continue building.
     */
    public Table primaryKey(String... columns)
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