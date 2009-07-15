package com.goodworkalan.addendum;

public class ExtensionElement
{
    private Schema schema;
    
    public ExtensionElement()
    {
    }
    
    protected void ending(Schema schema)
    {
    }
    
    public void setSchema(Schema schema)
    {
        if (schema != null)
        {
            throw new IllegalStateException();
        }
        this.schema = schema;
    }
    
    public Schema end()
    {
        ending(schema);
        return schema;
    }
}
