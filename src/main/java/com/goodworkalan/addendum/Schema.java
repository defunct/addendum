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
    public NewTable createTable(String name)
    {
        List<DefineColumn<?, ?>> columns = new ArrayList<DefineColumn<?,?>>();
        List<String> primaryKey = new ArrayList<String>();
        updates.add(new TableCreate(name, columns, primaryKey));
        return new NewTable(this, name, columns, primaryKey);
    }

    public Table alterTable(String name)
    {
        return new Table(this, name, updates);
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
    
    public Schema execute(Runnable runnable)
    {
        Execution execution = new Execution(runnable);
        updates.add(execution);
        return this;
    }
    
    public <T> T run(T runnable)
    {
        return runnable;
    }
    
    public void commit()
    {
    }
}
