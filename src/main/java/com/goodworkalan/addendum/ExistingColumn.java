package com.goodworkalan.addendum;

public abstract class ExistingColumn<Container, Element> extends DefineColumn<Container, Element>
{
    public ExistingColumn(Container container, Column column)
    {
        super(container, column);
    }
    
    public Element type(int columnType)
    {
        column.setColumnType(columnType);
        return getElement();
    }
    
    public Element type(Class<?> nativeType)
    {
        column.setColumnType(nativeType);
        return getElement();
    }
}
