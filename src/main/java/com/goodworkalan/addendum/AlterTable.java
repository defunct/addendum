package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.List;

public class AlterTable
{
    private final Schema schema;

    private String tableName;
    
    private final List<Update> updates;
    
    private final List<DefineColumn<?, ?>> addColumns;

    private final List<DefineColumn<?, ?>> verifyColumns;
    
    public AlterTable(Schema schema, String name, List<Update> updates)
    {
        this.schema = schema;
        this.tableName = name;
        this.updates = updates;
        this.addColumns  = new ArrayList<DefineColumn<?,?>>();
        this.verifyColumns = new ArrayList<DefineColumn<?,?>>();
    }
    
    public AlterTable rename(String newName)
    {
        updates.add(new RenameTable(tableName, newName));
        tableName = newName;
        return this;
    }
    
    
    /**
     * Add a new column to the table named by the given table name with the
     * given name and given column type.
     * 
     * @param tableName
     *            The name of the table in which to add the new column.
     * @param name
     *            The column name.
     * @param columnType
     *            Type SQL column type.
     */
    public AddColumn addColumn(String name, int columnType)
    {
        AddColumn column = new AddColumn(this, name, columnType);
        addColumns.add(column);
        return column;
    }
    
    public AddColumn addColumn(String name, Class<?> columnType)
    {
        AddColumn column = new AddColumn(this, name, columnType);
        addColumns.add(column);
        return column;
    }
    
    public AlterColumn alterColumn(String name)
    {
        AlterColumn column = new AlterColumn(this, name);
        updates.add(new ColumnAlteration(tableName, name, column));
        return column;
    }
    
    public AlterColumn alterColumn(String oldName, String newName)
    {
        AlterColumn column = new AlterColumn(this, newName);
        updates.add(new ColumnAlteration(tableName, oldName, column));
        return column;
    }
    
    public VerifyColumn verifyColumn(String name, Class<?> columnType)
    {
        VerifyColumn column = new VerifyColumn(this, name, columnType);
        verifyColumns.add(column);
        return column;
    }
    
    public Schema end()
    {
        updates.add(new TableAlteration(tableName, addColumns, verifyColumns));
        return schema;
    }
}
