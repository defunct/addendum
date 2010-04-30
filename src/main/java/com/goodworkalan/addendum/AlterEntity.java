package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.COLUMN_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_EXISTS;

/**
 * Builds an entity alteration that can change the underlying table name, alter
 * properties, add new properties or drop properties.
 * 
 * @author Alan Gutierrez
 */
public class AlterEntity {
    /** The addendum builder to return when this builder terminates. */
    private final Addendum addendum;

    /** The database migration patch. */
    private final Patch patch;
    
    /** The entity to alter. */
    private final Entity entity;

    /**
     * Create an alter table builder that alters the given entity.
     * 
     * @param addendum
     *            The addendum builder to return when this builder terminates.
     * @param entity
     *            The entity to alter.
     * @param patch
     *            The database migration patch.
     */
    AlterEntity(Addendum addendum, Entity entity, Patch patch) {
        this.addendum = addendum;
        this.entity = entity;
        this.patch = patch;
    }

    /**
     * Rename the given property. Returns a property rename builder to specify
     * the new name.
     * 
     * @param from
     *            The name of the property to rename.
     * @return A property rename builder.
     */
    public RenameProperty rename(String from) {
        Column column = new Column(entity.getColumn(from));
        return new RenameProperty(this, patch, entity.tableName, column, from);
    }

    /**
     * Add a new column to the table with the given name, the given column name,
     * and given column type.
     * 
     * @param name
     *            The property name.
     * @param columnName
     *            The column name.
     * @param columnType
     *            The <code>java.sql.Type</code> column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, String columnName, int columnType) {
        if (entity.properties.containsKey(name)) {
            throw new AddendumException(PROPERTY_EXISTS, name);
        }
        if (entity.columns.containsKey(columnName)) {
            throw new AddendumException(COLUMN_EXISTS, name);
        }
        Column column = new Column(name, columnType);
        return new AddProperty(this, patch, entity.tableName, name, column);
    }
    
    /**
     * Add a new column to the table with the given name
     * and given column type. The property name is used for the column name. 
     * 
     * @param name
     *            The property name.
     * @param columnType
     *            The <code>java.sql.Type</code> column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, int columnType) {
        return add(name, name, columnType);
    }

    /**
     * Add a new column to the table with the given name, the given column name,
     * and a column type appropriate for the given Java primitive.
     * 
     * @param name
     *            The property name.
     * @param columnName
     *            The column name.
     * @param nativeType
     *            The native column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, String columnName, Class<?> nativeType) {
        return add(name, columnName, Column.getColumnType(nativeType));
    }

    /**
     * Add a new column to the table with the given name and a column type
     * appropriate for the given Java primitive. The property name is used for
     * the column name.
     * 
     * @param name
     *            The property name.
     * @param nativeType
     *            The native column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, Class<?> nativeType) {
        return add(name, name, nativeType);
    }

    /**
     * Begin an alter column statement to alter the column with the given name.
     * 
     * @param property
     *            The name of the property to alter.
     * @return An alter column language element to define the column changes.
     */
    public AlterProperty alter(String property) {
        Column column = new Column(entity.getColumn(property));
        return new AlterProperty(this, patch, entity.tableName, column);
    }

    /**
     * Drop the property with the given property name. If the property does not
     * exist in the entity, an exception is raised.
     * 
     * @param property
     *            The property to drop.
     * @return This alter entity builder to continue construction.
     * @exception AddendumException
     *                If the property does not exist.
     */
    public AlterEntity drop(String property) {
        patch.add(new ColumnDrop(entity.tableName, property));
        return this;
    }

    /**
     * Terminate the builder and return the parent alter table builder.
     * 
     * @return The parent alter table builder to continue construction.
     */
    public Addendum end () {
        return addendum;
    }
}
