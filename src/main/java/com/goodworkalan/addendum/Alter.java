package com.goodworkalan.addendum;

/**
 * An alter element in the domain-specific language that prevents the creation
 * of new tables in an addendum after alterations have been defined.
 * 
 * @author Alan Gutierrez
 */
public interface Alter extends Verify
{
    /**
     * Begin an alter extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Alter>> T alter(T extension);
    
    /**
     * Alter an existing table with the given name.
     * 
     * @param name
     *            The table name.
     * @return An alter table element to specifiy changes to the table.
     */
    public AlterTable alterTable(String name);
}
