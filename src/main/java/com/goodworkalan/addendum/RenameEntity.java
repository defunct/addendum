package com.goodworkalan.addendum;

/**
 * Builds an entity rename specifying the new name for the entity.
 *
 * @author Alan Gutierrez
 */
public class RenameEntity {
    /** The addendum builder to return when this builder terminates. */
    private final Addendum addendum;

    /** The current entity name. */
    private final String from;

    /** The database migration script. */
    private final Patch script;

    /**
     * Create a new entity rename builder for the entity with the given current
     * name that adds an entity rename update to the given migration script. The
     * given addendum builder is returned when this builder terminates.
     * 
     * @param addendum
     *            The addendum builder to return when this builder terminates.
     * @param script
     *            The database migration script.
     * @param from
     *            The current entity name.
     */
    public RenameEntity(Addendum addendum, Patch script, String from) {
        this.addendum = addendum;
        this.from = from;
        this.script = script;
    }

    /**
     * Specify the name to rename the entity to.
     * 
     * @param to
     *            The new entity name.
     * @return The parent addendum builder to continue construction.
     */
    public Addendum to(String to) {
        script.add(new AliasRename(from, to));
        Schema schema = script.schema;
        Entity entity = schema.entities.get(schema.aliases.get(to));
        if (entity.tableName.equals(from)) {
            script.add(new TableRename(to, from, to));
        }
        return addendum;
    }
}
