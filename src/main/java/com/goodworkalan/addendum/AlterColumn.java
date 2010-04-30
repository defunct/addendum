package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that alters existing columns in an
 * existing table.
 * 
 * @author Alan Gutierrez
 */
public class AlterColumn extends ExistingColumn<AlterEntity, AlterColumn>
{
    private final Script script;
    
    private final String tableName;

    /**
     * Create an alter column element that alters the given column in the given
     * table.
     * 
     * @param table
     *            The table name.
     * @param column
     *            The column name.
     */
    public AlterColumn(AlterEntity alterTable, Script script, String tableName, Column column) {
        super(alterTable, column);
        this.tableName = tableName;
        this.script = script;
    }

    /**
     * Rename the column to the given name.
     * 
     * @param name
     *            The new column name.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public AlterColumn rename(String name)
    {
        column.setName(name);
        return this;
    }

    /**
     * Return the column specification language element to continue the
     * domain-specific language statement.
     * 
     * @return The column specification language element.
     */
    @Override
    protected AlterColumn getElement()
    {
        return this;
    }
    
    @Override
    protected void ending() {
        script.add(new ColumnAlteration(tableName, column.getName(), column));
    }
}
