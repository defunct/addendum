package com.goodworkalan.addendum;

/**
 * An interface for creating objects in the database, exposed by the
 * {@link Addendum} class, that extends other winnowing interfaces that guide
 * the domain-specific language through the required order for migration action
 * types.
 * 
 * @author Alan Gutierrez
 */
public interface Create extends Verify
{
    /**
     * Begin a creation extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Create>> T create(T extension);
    
    /**
     * Create a new table with the given name.
     * 
     * @param name
     *            The table name.
     * @return A create table element to define the new table.
     */
    public CreateTable table(String name);
}