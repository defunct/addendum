package com.goodworkalan.addendum;

public class AlterColumn extends DefineColumn<AlterTable, AlterColumn>
{
    public AlterColumn(AlterTable table, String name)
    {
        super(table, name, 0);
    }
    
    public AlterColumn type(int columnType)
    {
        setColumnType(columnType);
        return this;
    }
    
    public AlterColumn type(Class<?> columnType)
    {
        setColumnType(DefineColumn.getColumnType(columnType));
        return this;
    }

    @Override
    protected AlterColumn getBuilder()
    {
        return this;
    }
}
