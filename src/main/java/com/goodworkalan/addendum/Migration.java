package com.goodworkalan.addendum;


/**
 * Definition of an entire migration for the life of a database schema.
 * <p>
 * This is an alternate use of the domain-specific language that reduces some of
 * the verbosity. By deriving from this class, you won't have to indent quite so
 * far to get a new addendum.
 * 
 * @author Alan Gutierrez
 */
public abstract class Migration {
    /** The addenda. */
    private final Addenda addenda;

    /**
     * Create a migration using the given addenda.
     * 
     * @param addenda
     *            The addenda.
     */
    public Migration(Addenda addenda) {
        this.addenda = addenda;
    }

    /**
     * Derived classes build the migration by overriding this method.
     */
    public abstract void create();

    /**
     * Create a new addendum that will changes to a the database associated with
     * the connector of this addenda, or to any other data resources in the
     * application.
     * 
     * @return An addendum builder used to specify updates to the database.
     */
    public Addendum addendum() {
        return addenda.addendum();
    }
}
