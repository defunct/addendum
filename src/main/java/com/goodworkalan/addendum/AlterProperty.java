package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that alters existing columns in an
 * existing table.
 * 
 * @author Alan Gutierrez
 */
public class AlterProperty
extends ExistingProperty<AlterEntity, AlterProperty> {
    /** The database migration. */
    private final Patch patch;
   
    /** The property name. */
    private final String propertyName;
    
    /** The entity table name. */
    private final String tableName;

    /**
     * Create an alter column element that alters the given property and the
     * given column in the entity with the given table name and adds an alter
     * update to the given patch. The given parent alter table builder is
     * returned with this builder terminates.
     * 
     * @param alterTable
     *            The parent alter table builder.
     * @param patch
     *            The database migration.
     * @param table
     *            The entity table name.
     * @param propertyName
     *            The property name.
     * @param column
     *            The column definition.
     */
    public AlterProperty(AlterEntity alterTable, Patch patch, String tableName, String propertyName, Column column) {
        super(alterTable, column);
        this.tableName = tableName;
        this.propertyName = propertyName;
        this.patch = patch;
    }

    /**
     * Rename the database column to the given name.
     * 
     * @param name
     *            The new column name.
     * @return This alter column builder to continue construction.
     */
    public AlterProperty column(String name) {
        column.setName(name);
        return this;
    }

    /**
     * Return this builder used to continue construction.
     * 
     * @return This builder.
     */
    @Override
    protected AlterProperty getElement() {
        return this;
    }
    
    /**
     * Overridden to add a column alteration update to the addendum patch when
     * the builder terminates.
     */
    @Override
    protected void ending() {
        patch.add(new ColumnAlteration(tableName, propertyName, column));
    }
}
