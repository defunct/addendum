package com.goodworkalan.addendum.jpa;

public class DefaultValue
{
    private Object value;
    
    private boolean specified;
    
    public DefaultValue()
    {
    }
    
    public void setValue(Object value)
    {
        setSpecified(true);
        this.value = value;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public boolean isSpecified()
    {
        return specified;
    }

    public void setSpecified(boolean specified)
    {
        this.specified = specified;
    }
}
