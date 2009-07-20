package com.goodworkalan.addendum;

/**
 * Sub-classes of this element extend the domain-specific language.
 *  
 * @author Alan Gutierrez
 */
public class ExtensionElement
{
    /** The root domain-specific language element for this addendum. */
    private Addendum addendum;
    
    /**
     * Create a new extension element.
     */
    public ExtensionElement()
    {
    }

    /**
     * Called when the end of the statement is called so that the extension
     * element can define actions using the addendum.
     * 
     * @param addendum
     *            The addendum.
     */
    protected void ending(Addendum addendum)
    {
    }

    /**
     * Called from the addendum when the extension statement begins to set the
     * addendum that is passed to {@link #ending(Addendum) ending}.
     * 
     * @param addendum
     *            The addendum.
     */
    void setAddendum(Addendum addendum)
    {
        if (addendum != null)
        {
            throw new IllegalStateException();
        }
        this.addendum = addendum;
    }

    /**
     * Terminate the extension statement by returning the {@link Extend}
     * interface of the addedmum.
     * 
     * @return The {@link Extend} interface of the {@link Addendum}.
     */
    public Extend end()
    {
        ending(addendum);
        return addendum;
    }
}
