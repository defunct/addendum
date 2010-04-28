package com.goodworkalan.addendum;

import java.util.LinkedList;
import java.util.Map;

/**
 * The root object of the domain-specific language used to define a database
 * migration.
 * 
 * @author Alan Gutierrez
 */
public class Addendum {
    /** A list of updates to perform. */
    private final Script script;
    
    /**
     * A list of maps of table definitions by table name, one map for each
     * addendum.
     */
    private final LinkedList<Map<String, Table>> tables;
    
    /**
     * Create a new schema.
     * 
     * @param updates
     *            A list to record the updates to perform.
     */
    Addendum(Script script, LinkedList<Map<String, Table>> tables) {
        this.script = script;
        this.tables = tables;
    }

    /**
     * Performs updates using application specific SQL statements.
     * 
     * @param executable
     *            An {@link Executable} to execute.
     * @return This schema element to continue the domain-specific language
     *         statement.
     */
    public Addendum execute(Executable executable)
    {
        Execution execution = new Execution(executable);
        script.add(execution);
        return this;
    }

    /**
     * Create a new table with the given name.
     * 
     * @param name
     *            The table name.
     * @return A create table element to define the new table.
     */
    public TableElement table(String name) {
        Table table = tables.getFirst().get(name);
        if (table == null) {
            table = new Table(name);
            tables.getFirst().put(name, table);
        }
        tables.getFirst().put(name, table);
        return new TableElement(this, script, table);
    }

    /**
     * Alter an existing table with the given name.
     * 
     * @param name
     *            The table name.
     * @return An alter table element to specify changes to the table.
     */
//    public AlterTable alterTable(String name)
//    {
//        if (!tables.getFirst().containsKey(name))
//        {
//            tables.getFirst().put(name, new Table(name));
//        }
//        // FIXME Do not allow rename during first addendum.
//        return new AlterTable(this, name, updates, tables);
//    }

    /**
     * Create an insert statement that will insert values into the database.
     * 
     * @param table
     *            The name of the table to update.
     * @return An insert element to define the insert statement.
     */
    public Insert insert(String table)
    {
        Insertion insertion = new Insertion(table);
        script.add(insertion);
        return new Insert(this, insertion);
    }
    
    public Alteration alter() {
        return new Alteration(this, script);
    }
    
    /**
     * Terminates the addendum specification statement in the domain specific
     * language.
     */
    public void commit()
    {
    }
}
