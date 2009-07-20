package com.goodworkalan.addendum;

/**
 * The initial interface exposed by the {@link Addendum} class, that extends
 * other winnowing interfaces that guide the domain-specific language through
 * the required order for migration action types.
 * 
 * @author Alan Gutierrez
 */
public interface Extend extends Execute
{
    /**
     * Begin an extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement> T run(T extension);
}
