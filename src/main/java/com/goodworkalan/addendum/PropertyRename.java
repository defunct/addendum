package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.PROPERTY_EXISTS;

/**
 * An update that renames an property in an entity in the schema.
 * 
 * @author Alan Gutierrez
 */
class PropertyRename implements UpdateSchema {
    /** The name of the entity table. */
    private final String tableName;

    /** The current property name. */
    private final String from;

    /** The new property name. */
    private final String to;

    /**
     * Create an property rename update that renames the property in the entity
     * associated with given entity table name from the name given in from to
     * the name given in to.
     * 
     * @param tableName
     *            The name of the entity table.
     * @param from
     *            The current property name.
     * @param to
     *            The new property name.
     */
    public PropertyRename(String tableName, String from, String to) {
        this.tableName = tableName;
        this.from = from;
        this.to = to;
    }

    /**
     * Update rename the property for the entity table name in the given
     * tracking schema.
     * <p>
     * The database update returned is a no-op. This update only updates the
     * tracking schema.
     * 
     * @param schema
     *            The tracking schema.
     * @return An update to be performed against the database.
     */
    public DatabaseUpdate execute(Schema schema) {
        Entity entity = schema.entities.get(tableName);
        if (entity.properties.containsKey(to)) {
            throw new AddendumException(PROPERTY_EXISTS, to);
        }
        schema.aliases.put(to, schema.aliases.remove(from));
        return new NullDatabaseUpdate();
    }
}
