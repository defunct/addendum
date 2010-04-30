package com.goodworkalan.addendum;

/**
 * An element in the domain-specific language that alters existing columns in an
 * existing table.
 * 
 * @author Alan Gutierrez
 */
public class AlterProperty
extends ExistingColumn<AlterEntity, AlterProperty> {
    /** The database migration. */
    private final Script script;
    
    /** The entity table name. */
    private final String tableName;

    /**
     * Create an alter column element that alters the given column in the entity
     * with the given table name and adds an alter update to the given script.
     * The given parent alter table builder is returned with this builder
     * terminates.
     * 
     * @param alterTable
     *            The parent alter table builder.
     * @param script
     *            The database migration.
     * @param table
     *            The entity table name.
     * @param column
     *            The column definition.
     */
    public AlterProperty(AlterEntity alterTable, Script script, String tableName, Column column) {
        super(alterTable, column);
        this.tableName = tableName;
        this.script = script;
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
     * Overridden to add a column alteration update to the addendum script when
     * the builder terimates.
     */
    @Override
    protected void ending() {
        script.add(new ColumnAlteration(tableName, column.getName(), column));
    }
}
