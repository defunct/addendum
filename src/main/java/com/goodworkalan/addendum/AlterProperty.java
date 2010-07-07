package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

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
   
    /** The entity table. */
    private final Entity entity;
    
    /** The new column name, if it is renamed. */
    private String newColumnName;

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
     * @param entity
     *            The entity.
     * @param column
     *            The column definition.
     */
    public AlterProperty(AlterEntity alterTable, Patch patch, Entity entity, Column column) {
        super(alterTable, column);
        this.patch = patch;
        this.entity = entity;
        this.newColumnName = column.getName();
    }

    /**
     * Rename the database column to the given name.
     * 
     * @param name
     *            The new column name.
     * @return This alter column builder to continue construction.
     */
    public AlterProperty column(String name) {
        newColumnName = name;
        return this;
    }

    /**
     * Rename the property independently of the underlying column name.
     * 
     * @param name
     *            The new property name.
     * @return This alter column builder to continue construction.
     */
    public AlterProperty name(String name) {
        entity.rename(entity.getPropertyName(column.getName()), name);
        return this;
    }

    /**
     * Return this builder used to continue construction.
     * 
     * @return This builder.
     */
    @Override
    protected AlterProperty getSelf() {
        return this;
    }
    
    /**
     * Overridden to add a column alteration update to the addendum patch when
     * the builder terminates.
     */
    @Override
    protected void ending() {
        patch.add(new ColumnAlteration(entity.tableName, column, newColumnName));
    }
}
