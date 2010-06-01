package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.COLUMN_EXISTS;
import static com.goodworkalan.addendum.AddendumException.*;

import com.goodworkalan.addendum.dialect.Column;

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
     * Rename the underlying table.
     * 
     * @param tableName
     *            The new table name.
     * @return This alter entity builder to continue construction.
     */
    public AlterEntity table(String tableName) {
        patch.add(new TableRename(patch.schema.getEntityName(entity.tableName), entity.tableName, tableName));
        return this;
    }

    /**
     * Rename the entity independently of the table. This differs from the
     * {@link Addendum#rename(String, String) Addendum.rename} method in that it
     * <em>will not</em> also rename the underlying table if the underlying
     * table has the name name.
     * 
     * @param name
     *            The new entity name.
     * @return This alter entity builder to continue construction.
     */
    public AlterEntity name(String name) {
        patch.schema.rename(patch.schema.getEntityName(entity.tableName), name);
        return this;
    }

    /**
     * Rename the given property. Returns a property rename builder to specify
     * the new name.
     * 
     * @param from
     *            The name of the property.
     * @param to
     *            The name to rename the property to.
     * @return A property rename builder.
     */
    public AlterEntity rename(String from, String to) {
        if (!entity.properties.containsKey(from)) {
            throw new AddendumException(PROPERTY_MISSING, from);
        }
        entity.rename(from, to);
        Column renamed = entity.getColumn(to);
        if (renamed.getName().equals(from)) {
            patch.add(new ColumnAlteration(entity.tableName, renamed, to));
        }
        return this;
    }

    /**
     * Sets the association between the given column name to the given property
     * name, replacing the current column name to property name association. If
     * the property name is already in use, and is not associated with the given
     * column name, an exception is thrown. If the column name is not defined an
     * exception is thrown.
     * <p>
     * This method is useful when first mapping a legacy database with SQL like
     * names to Java object fields and bean properties.
     * 
     * @param columnName
     *            The column name.
     * @param propertyName
     *            The property name.
     * @return This alter entity builder to continue construction.
     * @exception AddendumException
     *                If the property name is already in use or if the column
     *                name cannot be found.
     */
    public AlterEntity alias(String columnName, String propertyName) {
        String existingColumnName = entity.properties.get(propertyName);
        if (existingColumnName == null) {
            if (!entity.columns.containsKey(columnName)) {
                throw new AddendumException(COLUMN_MISSING, columnName);
            }
            entity.properties.remove(entity.getPropertyName(columnName));
            entity.properties.put(propertyName, columnName);
        } else if (!existingColumnName.equals(columnName)) {
            throw new AddendumException(PROPERTY_EXISTS, propertyName);
        }
        return this;
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
        return new AlterProperty(this, patch, entity, entity.getColumn(property));
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
