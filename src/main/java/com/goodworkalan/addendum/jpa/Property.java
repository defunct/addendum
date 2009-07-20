package com.goodworkalan.addendum.jpa;

/**
 * A property element in the extension domain-specific language.
 * 
 * @author Alan Gutierrez
 */
public class Property<Parent>
{
    /** The parent element in the extension domain-specific language. */
    private final Parent parent;
    
    /** The default value reference. */
    private final DefaultValue defaultValue;

    /**
     * Create a property element in the extension domain-specific language.
     * 
     * @param parent
     *            The parent element in the extension domain-specific language.
     * @param defaultValue
     *            The default value reference.
     */
    public Property(Parent parent, DefaultValue defaultValue)
    {
        this.parent = parent;
        this.defaultValue = defaultValue;
    }
    
    /**
     * Set a default value for the mapped database column.
     * @param value The default value.
     * @return This property element to continue the proeprty statement.
     */
    public Property<Parent> defaultValue(Object value)
    {
        defaultValue.setValue(value);
        return this;
    }

    /**
     * Terminate the property statement by returning the parent element.
     * 
     * @return The parent element.
     */
    public Parent end()
    {
        return parent;
    }
}
