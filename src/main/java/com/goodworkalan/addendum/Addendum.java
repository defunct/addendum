package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.TABLE_MISSING;

import java.util.Map;

/**
 * The root builder for an individual migration definition.
 * 
 * @author Alan Gutierrez
 */
public class Addendum {
    /** A list of updates to perform. */
    private final Patch patch;

    /**
     * Create a new addendum using the given patch to record the addendum
     * properties.
     * 
     * @param patch
     *            The database migration patch.
     */
    Addendum(Patch patch) {
        this.patch = patch;
    }

    /**
     * Apply the given definition class against this addendum. Definition
     * classes are used to capture entity definitions generated from reflecting
     * on an object-relational mapping.
     * 
     * @param definition
     *            A definition.
     * @return This addendum builder to continue construction.
     */
    public Addendum apply(Definition definition) {
        definition.define(this);
        return this;
    }

    /**
     * Define an entity with the given entity name and the given table name in
     * the database.
     * 
     * @param name
     *            The entity name.
     * @param tableName
     *            The name of the table in the database.
     * @return An entity definition builder.
     */
    public DefineEntity define(String name, String tableName) {
        if (patch.aliases.put(name, tableName) != null) {
            throw new AddendumException(ADDENDUM_ENTITY_EXISTS, name);
        }
        Entity entity = new Entity(name);
        if (patch.entities.put(tableName, entity) != null) {
            throw new AddendumException(ADDENDUM_TABLE_EXISTS, tableName);
        }
        return new DefineEntity(this, entity);
    }

    /**
     * Define an entity with the given entity name which will also be used as
     * the table name in the database.
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
        for (Map.Entry<String, String> entry : patch.aliases.entrySet()) {
            String entityName = entry.getKey();
            if (!patch.schema.aliases.containsKey(entityName)) {
                String tableName = entry.getValue();
                if (patch.schema.entities.containsKey(tableName)) {
                    throw new AddendumException(TABLE_EXISTS, tableName);
                }
                patch.add(new TableCreate(entityName, patch.entities.get(tableName)));
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
            patch.add(new TableCreate(name, patch.getEntity(name)));
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
        if (patch.schema.aliases.containsKey(name)) {
            throw new AddendumException(ENTITY_EXISTS, name);
        }
        if (patch.schema.entities.containsKey(tableName)) {
            throw new AddendumException(TABLE_EXISTS, tableName);
        }
        return new CreateEntity(this, new Entity(tableName), name, patch);
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
     * @param to
     *            The name to rename the entity to.
     * @return This addendum builder to continue construction.
     */
    public Addendum rename(String from, String to) {
        if (!patch.schema.aliases.containsKey(from)) {
            throw new AddendumException(ENTITY_MISSING, from);
        }
        patch.schema.rename(from, to);
        Schema schema = patch.schema;
        Entity entity = schema.entities.get(schema.aliases.get(to));
        if (entity.tableName.equals(from)) {
            patch.add(new TableRename(to, from, to));
        }
        return this;
    }

    /**
     * Sets the association between the given table name to the given entity
     * name, replacing the current entity name to table name association. If the
     * entity name is already in use, and is not associated with the given table
     * name, an exception is raised. If the table name is not defined an
     * exception is raised.
     * <p>
     * This method is useful when first mapping a legacy database with SQL like
     * table names to Java objects.
     * 
     * @param tableName
     *            The table name.
     * @param entityName
     *            The entity name.
     * @return This addendum builder to continue construction.
     * @exception AddendumException
     *                If the entity name is already in use or if the table name
     *                cannot be found.
     */
    public Addendum alias(String tableName, String entityName) {
        // Didn't bother to create a schema update for this, since there is
        // never a database operation and no need to reuse.
        String existingEntityName = patch.schema.aliases.get(entityName);
        if (existingEntityName == null) {
            if (!patch.schema.entities.containsKey(tableName)) {
                throw new AddendumException(TABLE_MISSING, tableName);
            }
            patch.schema.aliases.remove(patch.schema.getEntityName(tableName));
            patch.schema.aliases.put(entityName, tableName);
        } else if (!existingEntityName.equals(tableName)) {
            throw new AddendumException(ENTITY_EXISTS, entityName);
        }
        return this;
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
        return new AlterEntity(this, patch.schema.getEntity(name), patch);
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
        patch.add(execution);
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
        patch.add(insertion);
        return new Insert(this, insertion);
    }

    /**
     * Terminates the addendum specification statement in the domain specific
     * language.
     */
    public void commit() {
    }
}
