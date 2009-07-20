package com.goodworkalan.addendum;

/**
 * Sub-classes of this element extend the domain-specific language.
 *  
 * @author Alan Gutierrez
 */
public class ExtensionElement
{
    private Addendum addendum;
    
    public ExtensionElement()
    {
    }
    
    protected void ending(Addendum addendum)
    {
    }
    
    public void setAddendum(Addendum addendum)
    {
        if (addendum != null)
        {
            throw new IllegalStateException();
        }
        this.addendum = addendum;
    }
    
    public Extend end()
    {
        ending(addendum);
        return addendum;
    }
}
