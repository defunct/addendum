package com.goodworkalan.addendum;

import java.util.Map;

import com.goodworkalan.danger.Danger;

/**
 * The root builder for an individual migration definition.
 * 
 * @author Alan Gutierrez
 */
public class Addendum {
    /** A list of updates to perform. */
    private final Patch patch;
    /** An entity name already exists in the addendum. */
    public final static String ADDENDUM_ENTITY_EXISTS = "403";
    /** The entity cannot be found in the addendum. */
    public final static String ADDENDUM_ENTITY_MISSING = "413";
    /** A table name already exists in the addendum. */
    public final static String ADDENDUM_TABLE_EXISTS = "404";
    /** The table cannot be found in the addendum. */
    public final static String ADDENDUM_TABLE_MISSING = "414";
    /** Unable to add a column. */
    public final static String CANNOT_ADD_COLUMN = "504";
    /** Unable to alter a column. */
    public final static String CANNOT_ALTER_COLUMN = "503";
    /** Unable to create the table for an entity due to an SQL exception. */
    public final static String CANNOT_CREATE_TABLE = "501";
    /** Unable to drop a column. */
    public final static String CANNOT_DROP_COLUMN = "505";
    /** Unable to execute arbitrary SQL statements. */
    public final static String CANNOT_EXECUTE_SQL = "502";
    /** Unable to insert values. */
    public final static String CANNOT_INSERT = "506";
    /** Unable to rename a table. */
    public final static String CANNOT_RENAME_TABLE = "507";
    /** A column already exists in the table in the schema. */
    public final static String COLUMN_EXISTS = "408";
    /** A column does not exist the entity. */
    public final static String COLUMN_MISSING = "416";
    /** The requested generator type is not supported by the SQL dialect. */
    public final static String DIALECT_DOES_NOT_SUPPORT_GENERATOR = "101";
    /** The dialog does not support a specific SQL type. */
    public final static String DIALECT_DOES_NOT_SUPPORT_TYPE = "102";
    /** An entity name already exists in the schema. */
    public final static String ENTITY_EXISTS = "405";
    /** The entity cannot be found in the schema. */
    public final static String ENTITY_MISSING = "410";
    /** Insert statement DSL values count does not match column count. */
    public final static String INSERT_VALUES = "401";
    /** Unable to open an SQL connection due to a JNI naming error. */
    public final static String NAMING_EXCEPTION = "201";
    /** A primary key property does not exist. */
    public final static String PRIMARY_KEY_COLUMN_MISSING = "417";
    /** The primary key has already been specified for the entity. */
    public final static String PRIMARY_KEY_EXISTS = "409";
    /** A property already exists in the entity. */
    public final static String PROPERTY_EXISTS = "407";
    /** A property does not exist the entity. */
    public final static String PROPERTY_MISSING = "415";
    /** Unable to determine the maximum value of the applied updates. */
    public final static String SQL_ADDENDA_COUNT = "303";
    /** Unable to update the addenda table with a new update. */
    public final static String SQL_ADDENDUM = "304";
    /** Unable to close a JDBC data source. */
    public final static String SQL_CLOSE = "399";
    /** Unable to connect to a JDBC data source. */
    public final static String SQL_CONNECT = "301";
    /** Unable to create the addenda table to track updates. */
    public final static String SQL_CREATE_ADDENDA = "302";
    /** Unable to execute a SQL migration statement. */
    public final static String SQL_EXECUTION = "308";
    /** Unable to create database dialect. */
    public final static String SQL_GET_DIALECT = "309";
    /** A table name already exists in the schema. */
    public final static String TABLE_EXISTS = "406";
    /** The table cannot be found in the schema. */
    public final static String TABLE_MISSING = "411";
    /** Unable to determine an SQL type for a Java class. */
    public final static String UNMAPPABLE_TYPE = "412";

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
            throw new Danger(Addendum.class, ADDENDUM_ENTITY_EXISTS, name);
        }
        Entity entity = new Entity(name);
        if (patch.entities.put(tableName, entity) != null) {
            throw new Danger(Addendum.class, ADDENDUM_TABLE_EXISTS, tableName);
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
                    throw new Danger(Addendum.class, TABLE_EXISTS, tableName);
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
            throw new Danger(Addendum.class, ENTITY_EXISTS, name);
        }
        if (patch.schema.entities.containsKey(tableName)) {
            throw new Danger(Addendum.class, TABLE_EXISTS, tableName);
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
            throw new Danger(Addendum.class, ENTITY_MISSING, from);
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
     * @exception Addendum
     *                If the entity name is already in use or if the table name
     *                cannot be found.
     */
    public Addendum alias(String tableName, String entityName) {
        // Didn't bother to create a schema update for this, since there is
        // never a database operation and no need to reuse.
        String existingEntityName = patch.schema.aliases.get(entityName);
        if (existingEntityName == null) {
            if (!patch.schema.entities.containsKey(tableName)) {
                throw new Danger(Addendum.class, TABLE_MISSING, tableName);
            }
            patch.schema.aliases.remove(patch.schema.getEntityName(tableName));
            patch.schema.aliases.put(entityName, tableName);
        } else if (!existingEntityName.equals(tableName)) {
            throw new Danger(Addendum.class, ENTITY_EXISTS, entityName);
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
