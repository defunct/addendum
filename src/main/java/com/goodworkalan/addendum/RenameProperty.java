package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Builds a property rename specifying the new name of the property.
 * 
 * @author Alan Gutierrez
 */
public class RenameProperty {
    /** The parent alter entity builder. */
    private final AlterEntity alterEntity;

    /** The current property name. */
    private final String from;
    
    /** The entity table name. */
    private final String tableName;
    
    /** The database migration patch. */
    private final Patch patch;

    /** The column definition. */
    private final Column column;

    /**
     * Create a new property rename builder for the property in the entity
     * associated with the given table name with the given current name that
     * adds an entity rename update to the given migration patch. The given
     * alter entity builder is returned when this builder terminates.
     * 
     * @param alterEntity
     *            The parent alter entity builder.
     * @param patch
     *            The database migration patch.
     * @param tableName
     *            The entity table name.
     * @param column
     *            The column definition.
     * @param from
     *            The current property name.
     */
    public RenameProperty(AlterEntity alterTable, Patch patch, String tableName, Column column, String from) {
        this.alterEntity = alterTable;
        this.from = from;
        this.column = column;
        this.patch = patch;
        this.tableName = tableName;
    }

    /**
     * Specify the name to rename the property to.
     * 
     * @param to
     *            The new property name.
     * @return The parent alter entity builder to continue construction.
     */
    public AlterEntity to(String to) {
        patch.add(new PropertyRename(tableName, from, to));
        Entity entity = patch.schema.entities.get(tableName);
        if (entity.getColumn(from).getName().equals(from)) {
            column.setName(to);
            patch.add(new ColumnAlteration(tableName, from, column));
        }
        return alterEntity;
    }
}
