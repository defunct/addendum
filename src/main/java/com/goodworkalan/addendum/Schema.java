package com.goodworkalan.addendum;

import java.util.List;

/**
 * Root element of a domain-specific language element used to specify updates to
 * the database.
 * 
 * @author Alan Gutierrez
 */
public class Schema
{
    /** A list of verifications to perform after all addenda are complete. */
    private final List<Update> verifications;

    /**
     * Create the root element of a domain-specific language element used to
     * specify updates to the database that will add verifications to the given
     * list of updates.
     * 
     * @param verifications
     *            A list of verifications to perform after all addenda are
     *            complete.
     */
    public Schema(List<Update> verifications)
    {
        this.verifications = verifications;
    }

    /**
     * Begin an addenda verify extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Schema>> T verify(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * After all addenda are applied, verify that the table definition for the
     * given table name matches an expected table definition.
     * 
     * @param tableName
     *            The table name.
     * @return An assert table element to define the expected table definition.
     */
    public SchemaTable verifyTable(String tableName)
    {
        return new SchemaTable(this, verifications, tableName);
    }
}
