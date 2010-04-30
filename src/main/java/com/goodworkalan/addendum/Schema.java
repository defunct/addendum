package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.TABLE_MISSING;

import java.util.HashMap;
import java.util.Map;

/**
 * A schema used to track the changes to the database. This tracking schema is
 * the authoritative schema. Schema information is obtained by tracking the
 * changes specified the addenda.
 * <p>
 * Column alterations that specify only attributes to change use the tracking
 * schema to obtain the missing attributes instead of selecting meta data from
 * the underlying database.
 * 
 * @author Alan Gutierrez
 */
class Schema {
    /** The map of entities by table name. */
    public final Map<String, Entity> entities = new HashMap<String, Entity>();

    /** The map of entity names to table names. */
    public final Map<String, String> aliases = new HashMap<String, String>();

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
            throw new AddendumException(ENTITY_MISSING, name);
        }
        Entity entity = entities.get(tableName);
        if (entity == null) {
            throw new AddendumException(TABLE_MISSING, tableName);
        }
        return entity;
    }
}
