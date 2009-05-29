package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.List;

/**
 * The root object of the domain-specific language used by
 * {@link DatabaseAddendum} to define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class Schema
{
    /** A list of updates to perform. */
    private final List<Update> updates;

    /**
     * Create a new schema.
     * 
     * @param updates
     *            A list to record the updates to perform.
     */
    Schema(List<Update> updates)
    {
        this.updates = updates;
    }

    /**
     * Create a new table with the given name.
     * 
     * @param name
     *            The table name.
     * @return This schema to continue building.
     */
    public Table createTable(String name)
    {
        List<Column<?, ?>> columns = new ArrayList<Column<?,?>>();
        List<String> primaryKey = new ArrayList<String>();
        updates.add(new CreateTable(name, columns, primaryKey));
        return new Table(this, name, columns, primaryKey);
    }

    /**
     * Add a new column to the table named by the given table name with the
     * given name and given column type.
     * 
     * @param tableName
     *            The name of the table in which to add the new column.
     * @param name
     *            The column name.
     * @param columnType
     *            Type SQL column type.
     */
    public AddColumn addColumn(String tableName, String name, int columnType)
    {
        return new AddColumn(this, tableName, name, columnType);
    }

    /**
     * Create an insert statement that will insert values into the database.
     * 
     * @param table
     *            The name of the table to update.
     * @return An insert builder.
     */
    public Insert insert(String table)
    {
        Insertion insertion = new Insertion(table);
        updates.add(insertion);
        return new Insert(this, insertion);
    }
    
    public void commit()
    {
    }
}
