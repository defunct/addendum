package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    
    private final LinkedList<Map<String, Table>> tables;

    /**
     * Create a new schema.
     * 
     * @param updates
     *            A list to record the updates to perform.
     */
    Schema(List<Update> updates, LinkedList<Map<String, Table>> tables)
    {
        this.updates = updates;
        this.tables = tables;
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
        if (tables.getFirst().containsKey(name))
        {
            throw new IllegalStateException();
        }
        Table table = new Table(name);
        tables.getFirst().put(name, table);
        List<String> primaryKey = new ArrayList<String>();
        updates.add(new TableCreate(table, primaryKey));
        return new NewTable(this, table, primaryKey);
    }

    public AlterTable alterTable(String name)
    {
        if (!tables.getFirst().containsKey(name))
        {
            tables.getFirst().put(name, new Table(name));
        }
        // FIXME Do not allow rename during first addendum.
        return new AlterTable(this, name, updates, tables);
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

    /**
     * Performs updates using application specific SQL statements.
     * 
     * @param executable
     *            An exeuctable to execute.
     * @return This schema element to continue the domain-specific language
     *         statement.
     */
    public Schema execute(Executable executable)
    {
        Execution execution = new Execution(executable);
        updates.add(execution);
        return this;
    }

    /**
     * Terminates the addendum specification statement in the domain specific
     * langauge.
     */
    public void commit()
    {
    }
}
