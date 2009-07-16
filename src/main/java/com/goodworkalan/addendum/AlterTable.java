package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AlterTable
{
    private final Schema schema;

    private String tableName;
    
    private final List<Update> updates;
    
    private final List<Column> addColumns;

    private final List<Column> verifyColumns;
    
    private final LinkedList<Map<String, Table>> tables;
    
    public AlterTable(Schema schema, String name, List<Update> updates, LinkedList<Map<String, Table>> tables)
    {
        this.schema = schema;
        this.tableName = name;
        this.updates = updates;
        this.addColumns  = new ArrayList<Column>();
        this.verifyColumns = new ArrayList<Column>();
        this.tables = tables;
    }
    
    public AlterTable rename(String newName)
    {
        updates.add(new RenameTable(tableName, newName));
        tableName = newName;
        return this;
    }
    
    public AlterTable renameFrom(String oldName)
    {
        for (Map<String, Table>  map : tables)
        {
            Table table = map.get(tableName);
            if (table != null)
            {
                if (table.getName().equals(tableName))
                {
                    table.setName(oldName);
                }
                else
                {
                    break;
                }
            }
        }
        updates.add(new RenameTable(oldName, tableName));
        return this;
    }
    
    private Column newColumn(String name, int code)
    {
        Column column = tables.getFirst().get(tableName).getColumns().get(name);
        if (column == null)
        {
            column = new Column(name);
            tables.getFirst().get(tableName).getColumns().put(name, column);
            return column;
        }
        throw new AddendumException(code);
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
        Column column = newColumn(name, 0);
        column.setDefaults(columnType);
        addColumns.add(column);
        return new AddColumn(this, column);
    }
    
    public AddColumn addColumn(String name, Class<?> nativeType)
    {
        Column column = newColumn(name, 0);
        column.setDefaults(nativeType);
        addColumns.add(column);
        return new AddColumn(this, column);
    }
    
    public AlterColumn alterColumn(String name)
    {
        Column column = newColumn(name, 0);
        updates.add(new ColumnAlteration(tableName, name, column));
        return new AlterColumn(this, column);
    }
    
    public AlterColumn renameColumn(String oldName, String newName)
    {
        Column column = newColumn(oldName, 0);
        column.setName(newName);
        updates.add(new ColumnAlteration(tableName, oldName, column));
        return new AlterColumn(this, column);
    }
    
    private boolean renameColumnFrom(Column column, String newName, String oldName)
    {
        if (column != null)
        {
            if (column.getName().equals(newName))
            {
                column.setName(oldName);
            }
            else
            {
                return true;
            }
        }
        return false;
    }
    
    public AlterColumn renameColumnFrom(String newName, String oldName)
    {
        Column alter = newColumn(newName, 0);
        for (Map<String, Table> addendum : tables)
        {
            Table table = addendum.get(tableName);
            if (table != null)
            {
                if (renameColumnFrom(table.getColumns().get(newName), newName, oldName))
                {
                    break;
                }
                for (Map<String, Column> pair : table.getVerifications())
                {
                    for (Map.Entry<String, Column> entry : pair.entrySet())
                    {
                        if (entry.getKey().equals(newName) && renameColumnFrom(entry.getValue(), newName, oldName))
                        {
                            break;
                        }
                    }
                }
            }
        }
        updates.add(new ColumnAlteration(tableName, oldName, alter));
        return new AlterColumn(this, alter);
    }
    
    public VerifyColumn verifyColumn(String name, Class<?> nativeType)
    {
        Column column = new Column(name, nativeType);
        tables.getFirst().get(tableName).getVerifications().add(Collections.singletonMap(name, column));
        verifyColumns.add(column);
        return new VerifyColumn(this, column);
    }
    
    public Schema end()
    {
        updates.add(new TableAlteration(tableName, addColumns, verifyColumns));
        return schema;
    }
}
