package com.goodworkalan.addendum;

public class VerifyColumn extends FreshColumn<AlterTable, VerifyColumn>
{
    public VerifyColumn(AlterTable table, Column column)
    {
        super(table, column);
    }

    @Override
    protected VerifyColumn getElement()
    {
        return this;
    }
}
