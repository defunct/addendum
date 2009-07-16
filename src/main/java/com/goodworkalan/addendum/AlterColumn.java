package com.goodworkalan.addendum;

public class AlterColumn extends DefineColumn<AlterTable, AlterColumn>
{
    public AlterColumn(AlterTable table, Column column)
    {
        super(table, column);
    }
    
    public AlterColumn type(int columnType)
    {
        column.setColumnType(columnType);
        return this;
    }
    
    public AlterColumn type(Class<?> nativeType)
    {
        column.setColumnType(nativeType);
        return this;
    }

    @Override
    protected AlterColumn getBuilder()
    {
        return this;
    }
}
