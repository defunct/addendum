package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_MISSING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Script {
    public final Schema schema;
    
    public final Map<String, String> aliases = new HashMap<String, String>();

    public final Map<String, Entity> entities = new HashMap<String, Entity>();
    
    private final List<UpdateDatabase> databaseUpdates;
    
    public Script(Schema schema, List<UpdateDatabase> databaseUpdates) {
        this.schema = schema;
        this.databaseUpdates = databaseUpdates;
    }
    
    public void add(UpdateSchema update) {
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
