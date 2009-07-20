package com.goodworkalan.addendum;

/**
 * An interface for populating the database, exposed by the {@link Addendum}
 * class, that extends other winnowing interfaces that guide the domain-specific
 * language through the required order for migration action types.
 * 
 * @author Alan Gutierrez
 */
public interface Populate extends Commit
{
    /**
     * Begin an insert extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Populate>> T insert(T extension);

    /**
     * Create an insert statement that will insert values into the database.
     * 
     * @param table
     *            The name of the table to update.
     * @return An insert element to define the insert statement.
     */
    public Insert insert(String table);
}
