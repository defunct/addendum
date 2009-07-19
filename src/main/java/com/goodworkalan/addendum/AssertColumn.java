package com.goodworkalan.addendum;

public class AssertColumn extends ExistingColumn<AssertTable, AssertColumn>
{
    public AssertColumn(AssertTable table, Column column)
    {
        super(table, column);
    }

    @Override
    protected AssertColumn getElement()
    {
        return this;
    }
}
