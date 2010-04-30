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

    /** The database migration patch. */
    private final Patch patch;

    /**
     * Create a new entity rename builder for the entity with the given current
     * name that adds an entity rename update to the given migration patch. The
     * given addendum builder is returned when this builder terminates.
     * 
     * @param addendum
     *            The addendum builder to return when this builder terminates.
     * @param patch
     *            The database migration patch.
     * @param from
     *            The current entity name.
     */
    public RenameEntity(Addendum addendum, Patch patch, String from) {
        this.addendum = addendum;
        this.from = from;
        this.patch = patch;
    }

    /**
     * Specify the name to rename the entity to.
     * 
     * @param to
     *            The new entity name.
     * @return The parent addendum builder to continue construction.
     */
    public Addendum to(String to) {
        patch.add(new AliasRename(from, to));
        Schema schema = patch.schema;
        Entity entity = schema.entities.get(schema.aliases.get(to));
        if (entity.tableName.equals(from)) {
            patch.add(new TableRename(to, from, to));
        }
        return addendum;
    }
}
