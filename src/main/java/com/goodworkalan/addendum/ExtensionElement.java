package com.goodworkalan.addendum;

/**
 * Sub-classes of this element extend the domain-specific language.
 *  
 * @author Alan Gutierrez
 */
public class ExtensionElement<Parent>
{
    /** The root domain-specific language element for this addendum. */
    private Parent parent;
    
    /**
     * Create a new extension element.
     */
    public ExtensionElement()
    {
    }

    /**
     * Called when the end of the statement is called so that the extension
     * element can define actions using the parent.
     * 
     * @param parent
     *            The parent.
     */
    protected void ending(Parent parent)
    {
    }

    /**
     * Called from the parent element when the extension statement begins to set
     * the parent that is passed to {@link #ending(Parent) ending}.
     * 
     * @param parent
     *            The parent element.
     */
    void setAddendum(Parent parent)
    {
        if (parent != null)
        {
            throw new IllegalStateException();
        }
        this.parent = parent;
    }

    /**
     * Terminate the extension statement by returning the parent element.
     * 
     * @return The parent element.
     */
    public Parent end()
    {
        ending(parent);
        return parent;
    }
}
