package com.goodworkalan.addendum;

/**
 * An interface exposed by the {@link Addendum} class, that extends other
 * winnowing interfaces that guide the domain-specific language through the
 * required order for migration action types.
 * 
 * @author Alan Gutierrez
 */
public interface Execute extends Create
{
    /**
     * Begin a execute extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Execute>> T execute(T extension);
    
    /**
     * Performs updates using application specific SQL statements.
     * 
     * @param executable
     *            An {@link Executable} to execute.
     * @return This schema element to continue the domain-specific language
     *         statement.
     */
    public Execute execute(Executable executable);
}
