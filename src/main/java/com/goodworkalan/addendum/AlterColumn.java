package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that alters existing columns in an
 * existing table.
 * 
 * @author Alan Gutierrez
 */
public class AlterColumn extends DefineColumn<AlterTable, AlterColumn>
{
    /**
     * Create an alter column elemnet that alters the given column in the given
     * table.
     * 
     * @param table
     *            The table name.
     * @param column
     *            The column name.
     */
    public AlterColumn(AlterTable table, Column column)
    {
        super(table, column);
    }

    /**
     * Set the column type to the given column type according to
     * <code>java.sql.Types</code>.
     * 
     * @param columnType
     *            The column type.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public AlterColumn type(int columnType)
    {
        column.setColumnType(columnType);
        return this;
    }

    /**
     * Set the column type to the given to the <code>java.sql.Types</code>
     * column type mapped to the given native type.
     * 
     * @param columnType
     *            The column type.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public AlterColumn type(Class<?> nativeType)
    {
        column.setColumnType(nativeType);
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
}
