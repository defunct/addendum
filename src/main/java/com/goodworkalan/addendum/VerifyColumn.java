package com.goodworkalan.addendum;

public class VerifyColumn extends FreshColumn<Table, VerifyColumn>
{
    public VerifyColumn(Table table, String name, int columnType)
    {
        super(table, name, columnType);
    }
    
    public VerifyColumn(Table table, String name, Class<?> columnType)
    {
        this(table, name, DefineColumn.getColumnType(columnType));
    }

    @Override
    protected VerifyColumn getBuilder()
    {
        return this;
    }
}
