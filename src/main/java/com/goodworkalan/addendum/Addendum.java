package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.CREATE_DEFINITION;
import static com.goodworkalan.addendum.AddendumException.ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.TABLE_MISSING;

import java.util.Map;

import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * The root builder for an individual migration definition.
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
     * Create a new instance of a definition of the given type using the given
     * reflective factory.
     * <p>
     * This method wraps a reflective exception in an addendum exception and is
     * package visible for testing.
     * 
     * @param reflective
     *            The reflective factory.
     * @param definition
     *            The definition type.
     * @return A new instance of the definition type.
     */
    static Definition newInstance(ReflectiveFactory reflective, Class<? extends Definition> definition) {
        try {
            return reflective.newInstance(definition);
        } catch (ReflectiveException e) {
            throw new AddendumException(CREATE_DEFINITION, e, definition.getCanonicalName());
        }
    }

    /**
     * Apply the an instance of the given definition class against this
     * addendum. Definition classes are used to capture entity definitions
     * generated from reflecting on an object-relational mapping.
     * 
     * @param definition
     *            A definition class.
     * @return This addendum builder to continue construction.
     */
    public Addendum apply(Class<? extends Definition> definition) {
        newInstance(new ReflectiveFactory(), definition).define(this);
        return this;
    }

    /**
     * Define an entity with the given entity <code>name</code> and the given
     * <code>tableName</code> in the database.
     * <p>
     * FIXME Next, 100% coverage of DefineEntity.
     * @param name
     *            The entity name.
     * @param tableName
     *            The name of the table in the database.
     * @return An entity definition builder.
     */
    public DefineEntity define(String name, String tableName) {
        if (script.aliases.put(name, tableName) != null) {
            throw new AddendumException(ADDENDUM_ENTITY_EXISTS, name);
        }
        Entity entity = new Entity(name);
        if (script.entities.put(tableName, entity) != null) {
            throw new AddendumException(ADDENDUM_TABLE_EXISTS, tableName);
        }
        return new DefineEntity(this, entity);
    }

    /**
     * Define an entity with the given entity <code>name</code> which will also
     * be used as the table name in the database.
     * 
     * @param name
     *            The entity name.
     * @return An entity definition builder.
     */
    public DefineEntity define(String name) {
        return define(name, name);
    }

    /**
     * Create entities defined in this addendum that do not already exist in the
     * tracking schema.
     * 
     * @return This addendum builder to continue construction.
     */
    public Addendum createIfAbsent() {
        for (Map.Entry<String, String> entry : script.aliases.entrySet()) {
            String entityName = entry.getKey();
            if (!script.schema.aliases.containsKey(entityName)) {
                String tableName = entry.getValue();
                if (script.schema.entities.containsKey(tableName)) {
                    throw new AddendumException(TABLE_EXISTS, tableName);
                }
                script.add(new TableCreate(entityName, script.entities.get(tableName)));
            }
        }
        return this;
    }

    /**
     * Create entities in the schema using the entities defined in this addendum
     * given as a list of addendum entity names. If the entity does not exist in
     * the addendum an exception is raised. If the entity or the table already
     * exists in the schema an exception is raised.
     * 
     * @param names
     *            The list of addendum defined entities to create.
     * @return This addendum builder to continue construction.
     */
    public Addendum createDefinitions(String...names) {
        for (String name : names) {
            script.add(new TableCreate(name, script.getEntity(name)));
        }
        return this;
    }

    /**
     * Create an entity with the given entity name and the given table name in
     * the database.
     * 
     * @param name
     *            The entity name.
     * @param tableName
     *            The name of the table in the database.
     * @return A create entity builder.
     */
    public CreateEntity create(String name, String tableName) {
        if (script.schema.aliases.containsKey(name)) {
            throw new AddendumException(ENTITY_EXISTS, name);
        }
        if (script.schema.entities.containsKey(tableName)) {
            throw new AddendumException(TABLE_EXISTS, tableName);
        }
        return new CreateEntity(this, new Entity(tableName), name, script);
    }

    /**
     * Create an entity with the given entity name which will also be used as
     * the table name in the database.
     * 
     * @param name
     *            The entity name.
     * @return A create entity builder.
     */
    public CreateEntity create(String name) {
        return create(name, name);
    }

    /**
     * Rename the entity with the given name. This method returns a rename
     * builder that will be used to specify the new entity name.
     * <p>
     * If the table for the renamed entity has the same name as the entity, the
     * table is renamed to the new entity name. If the table has a different
     * name than the entity name, only the entity is renamed.
     * 
     * @param from
     *            The name of the entity to rename.
     * @return The entity rename builder.
     */
    public RenameEntity rename(String from) {
        if (!script.schema.aliases.containsKey(from)) {
            throw new AddendumException(TABLE_MISSING, from);
        }
        return new RenameEntity(this, script, from);
    }

    /**
     * Alter the entity with the given name. This method returns an entity
     * alteration builder that can be used to change the underlying table, alter
     * existing properties, add new properties, or drop properties.
     * 
     * @param name
     *            The name of the entity to alter.
     * @return An entity alteration builder.
     */
    public AlterEntity alter(String name) {
        return new AlterEntity(this, script.schema.getEntity(name), script);
    }

    /**
     * Performs updates using application specific SQL statements.
     * 
     * @param executable
     *            An {@link Executable} to execute.
     * @return This schema element to continue the domain-specific language
     *         statement.
     */
    public Addendum execute(Executable executable) {
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
    public Insert insert(String table) {
        Insertion insertion = new Insertion(table);
        script.add(insertion);
        return new Insert(this, insertion);
    }

    /**
     * Terminates the addendum specification statement in the domain specific
     * language.
     */
    public void commit() {
    }
}
