package com.goodworkalan.addendum;

public class ExtensionElement
{
    private Addendum schema;
    
    public ExtensionElement()
    {
    }
    
    protected void ending(Addendum schema)
    {
    }
    
    public void setSchema(Addendum schema)
    {
        if (schema != null)
        {
            throw new IllegalStateException();
        }
        this.schema = schema;
    }
    
    public Addendum end()
    {
        ending(schema);
        return schema;
    }
}
