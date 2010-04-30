package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ENTITY_EXISTS;

/**
 * An update that renames an entity name in the schema.
 *
 * @author Alan Gutierrez
 */
class AliasRename implements UpdateSchema {
    /** The existing alias name. */
    private final String from;
    
    /** The new alias name. */
    private final String to;

    /**
     * Create an alias rename update that renames the alias from the name given
     * in from to the name given in to.
     * 
     * @param from
     *            The current alias name.
     * @param to
     *            The new alias name.
     */
    public AliasRename(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Perform the update by moving the alias in the alias map in the schema. If
     * the new name already exists in the schema, an exception is raised.
     * <p>
     * The database update returned is a no-op. This update only updates the
     * tracking schema.
     * 
     * @param schema
     *            The tracking schema.
     * @return A no-op update to be performed against the database.
     * @exception AddendumException
     *                If the new name already exists in the schema.
     */
    public DatabaseUpdate execute(Schema schema) {
        if (schema.aliases.containsKey(to)) {
            throw new AddendumException(ENTITY_EXISTS, to);
        }
        schema.aliases.put(to, schema.aliases.remove(from));
        return new NullDatabaseUpdate();
    }
}
