package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_MISSING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of updates for a single migration. Updates are added to the s script by
 * (FIXME Rename migration) using the {@link #add(SchemaUpdate) add} method.
 * There are public members that represent the tracking schema and the entity
 * mappings defined in the current migration.
 * 
 * @author Alan Gutierrez
 */
class Script {
    /** The tracking schema. */
    public final Schema schema;

    /** The map of entity names to entity table names. */
    public final Map<String, String> aliases = new HashMap<String, String>();

    /** The map of entity table names to entity definitions. */
    public final Map<String, Entity> entities = new HashMap<String, Entity>();
    
    /** The list of database updates. */
    private final List<DatabaseUpdate> databaseUpdates;

    /**
     * Create a new migration with the given schema and record updates in the
     * given list of database updates.
     * 
     * @param schema
     *            The tracking schema.
     * @param databaseUpdates
     *            The list of database updates.
     */
    public Script(Schema schema, List<DatabaseUpdate> databaseUpdates) {
        this.schema = schema;
        this.databaseUpdates = databaseUpdates;
    }

    /**
     * Add a schema update to the migration. The tracking schema update is
     * applied immediately. The database update is applied to the database if it
     * has not already been applied.
     * 
     * @param update
     *            The schema update.
     */
    public void add(SchemaUpdate update) {
        databaseUpdates.add(update.execute(schema));
    }
    
    /**
     * Get the entity definition with the given entity name. The entity is found
     * by looking up the given name in the aliases map, then by looking up the
     * entity in the entities map with the name found in the aliases map. If
     * either lookup returns null, an exception is thrown.
     * 
     * @param name
     *            The entity name.
     * @return The entity associated with the name.
     * @exception AddendumException
     *                If the entity alias does not exist or the entity
     *                definition does not exist.
     */
    public Entity getEntity(String name) {
        String tableName = aliases.get(name);
        if (tableName == null) {
            throw new AddendumException(ADDENDUM_ENTITY_MISSING, name);
        }
        Entity entity = entities.get(tableName);
        if (entity == null) {
            throw new AddendumException(ADDENDUM_TABLE_MISSING, tableName);
        }
        return entity;
    }
}
