package com.goodworkalan.addendum;

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
     * Create a new schema.
     * 
     * @param updates
     *            A list to record the updates to perform.
     */
    Addendum(Script script) {
        this.script = script;
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
    
    public Addendum createIfAbsent() {
        for (Map.Entry<String, Table> entry : script.tables.entrySet()) {
            if (!script.database.tables.containsKey(entry.getKey())) {
                script.add(new TableCreate(entry.getValue()));
            }
        }
        return this;
    }
    
    public Addendum create(String...names) {
        for (String name : names) {
            Table table = script.database.tables.get(name);
            if (table == null) {
                throw new AddendumException(0, name);
            }
            script.add(new TableCreate(table));
        }
        return this;
    }
    
    public TableElement create(String name) {
        if (script.database.tables.containsKey(name)) {
            throw new AddendumException(0, name);
        }
        final Table table = new Table(name);
        return new TableElement(this, script, table, new Runnable() {
            public void run() {
                script.add(new TableCreate(table));
            }
        });
    }

    /**
     * Create a new table with the given name.
     * 
     * @param name
     *            The table name.
     * @return A create table element to define the new table.
     */
    public TableElement table(String name) {
        Table table = script.tables.get(name);
        if (table == null) {
            table = new Table(name);
            script.tables.put(name, table);
        }
        return new TableElement(this, script, table, new Runnable() {
            public void run() {
            }
        });
    }
    
    
    public RenameTable rename(String from) {
        return new RenameTable(this, script, from);
    }
    
    public AlterTable alter(String name) {
        Table table = script.database.tables.get(name);
        if (table == null) {
            throw new AddendumException(0, name);
        }
        return new AlterTable(this, table, script);
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
    
    /**
     * Terminates the addendum specification statement in the domain specific
     * language.
     */
    public void commit()
    {
    }
}
