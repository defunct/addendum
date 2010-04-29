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
        for (Map.Entry<String, Entity> entry : script.entities.entrySet()) {
            if (!script.schema.aliases.containsKey(entry.getKey())) {
                script.add(new TableCreate(entry.getKey(), entry.getValue()));
            }
        }
        return this;
    }
    
    public Addendum create(String...names) {
        for (String name : names) {
            String alias = script.schema.aliases.get(name);
            if (alias == null) {
                throw new AddendumException(0, name);
            }
            Entity table = script.schema.tables.get(alias);
            if (table == null) {
                throw new AddendumException(0, name, alias);
            }
            script.add(new TableCreate(alias, table));
        }
        return this;
    }
    
    public TableElement create(final String name) {
        if (script.schema.tables.containsKey(name)) {
            throw new AddendumException(0, name);
        }
        final Entity table = new Entity(name);
        return new TableElement(this, table, new Runnable() {
            public void run() {
                script.add(new TableCreate(name, table));
            }
        });
    }

    /**
     * Define an entity of the given name for use in this addendum.
     * <p>
     * FIXME Rename TableElement to DefineEntity.
     * 
     * @param name
     *            The entity name.
     * @return A create entity element to define the new table.
     */
    public TableElement define(String name, String tableName) {
        if (script.aliases.put(name, tableName) != null) {
            throw new AddendumException(0, name, tableName);
        }
        Entity entity = new Entity(name);
        if (script.entities.put(tableName, entity) != null) {
            throw new AddendumException(0, name, tableName);
        }
        return new TableElement(this, entity, new Runnable() {
            public void run() {
            }
        });
    }
    
    public TableElement define(String name) {
        return define(name, name);
    }
    
    
    public RenameTable rename(String from) {
        if (!script.schema.aliases.containsKey(from)) {
            throw new AddendumException(0, from);
        }
        return new RenameTable(this, script, from);
    }
    
    public AlterTable alter(String name) {
        String alias = script.schema.aliases.get(name);
        if (alias == null) {
            throw new AddendumException(0, name);
        }
        Entity table = script.schema.tables.get(alias);
        if (table == null) {
            throw new AddendumException(0, name, alias);
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
