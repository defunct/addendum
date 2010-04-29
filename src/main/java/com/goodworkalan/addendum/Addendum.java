package com.goodworkalan.addendum;

import java.util.Map;


/**
 * The root language element of an individual migration definition.
 * 
 * @author Alan Gutierrez
 */
public class Addendum {
    /** A list of updates to perform. */
    private final Script script;

    /**
     * Create a new addendum using the given <code>script</code> to record the
     * addendum properties.
     * 
     * @param script
     *            The database migration script.
     */
    Addendum(Script script) {
        this.script = script;
    }

    /**
     * Define an entity with the given entity <code>name</code> and the given
     * <code>tableName</code> in the database.
     * 
     * @param name
     *            The entity name.
     * @return A create entity element to define the new table.
     */
    public DefineEntity define(String name, String tableName) {
        if (script.aliases.put(name, tableName) != null) {
            throw new AddendumException(0, name, tableName);
        }
        Entity entity = new Entity(name);
        if (script.entities.put(tableName, entity) != null) {
            throw new AddendumException(0, name, tableName);
        }
        return new DefineEntity(this, entity);
    }

    /**
     * Define an entity with the given entity <code>name</code> which will also
     * be used as the table name in the database.
     * 
     * @param name
     *            The entity name.
     * @return An entity definition langauge element.
     */
    public DefineEntity define(String name) {
        return define(name, name);
    }

    // FIXME Take a set of names to create, or empty means all.
    public Addendum createIfAbsent() {
        // FIXME Broken, need to use aliases.
        for (Map.Entry<String, Entity> entry : script.entities.entrySet()) {
            if (!script.schema.aliases.containsKey(entry.getKey())) {
                script.add(new TableCreate(entry.getKey(), entry.getValue()));
            }
        }
        return this;
    }
    
    public Addendum createDefinitions(String...names) {
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
    
    public CreateEntity create(String name, String tableName) {
        if (script.schema.aliases.containsKey(name)) {
            throw new AddendumException(0, name, tableName);
        }
        if (script.schema.tables.containsKey(tableName)) {
            throw new AddendumException(0, name, tableName);
        }
        return new CreateEntity(this, new Entity(tableName), name, script);
    }

    public CreateEntity create(String name) {
        return create(name, name);
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
