package com.goodworkalan.addendum;

import java.util.Arrays;

/**
 * Builds a new entity specifying the entity primary key and entity properties.
 * <p>
 * This class is used in the context of a domain-specific language that is
 * implemented as chained Java method calls. Refer to the package documentation
 * for documentation of the language and examples of use.
 * 
 * @author Alan Gutierrez
 */
public class DefineEntity {
    /** The root individual migration builder. */
    private final Addendum addendum;
    
    /** The entity definition. */
    protected final Entity entity;
    
    /**
     * Create a table builder with the given root language element.
     * 
     * @param addendum
     *            The root individual migration builder.
     * @param entity
     *            The entity definition.
     */
    DefineEntity(Addendum addendum, Entity entity) {
        this.addendum = addendum;
        this.entity = entity;
    }

    /**
     * Define a new column in the table with the given name and given column
     * type stored in the given column name in the database.
     * 
     * @param name
     *            The column name.
     * @param columnName
     *            The name of the column in the database.
     * @param columnType
     *            The SQL column type.
     * @return A new property builder.
     */
    public CreateColumn add(String name, String columnName, int columnType) {
        if (entity.properties.containsKey(name)) {
            throw new AddendumException(0);
        }
        if (entity.columns.containsKey(columnName)) {
            throw new AddendumException(0);
        }
        entity.properties.put(name, columnName);
        Column column = new Column(columnName);
        entity.columns.put(columnName, column);
        column.setDefaults(columnType);
        return new CreateColumn(this, column);
    }

    /**
     * Define a new column in the table with the given name and given column
     * type.
     * 
     * @param name
     *            The property name.
     * @param columnType
     *            The SQL column type.
     * @return A new property builder.
     */
    public CreateColumn add(String name, int columnType) {
        return add(name, name, columnType);
    }

    /**
     * Define a new property in the entity with the given name and a
     * <code>java.sql.Types</code> column type appropriate for the given Java
     * native type stored in the given column name in the database.
     * 
     * @param name
     *            The property name.
     * @param columnName
     *            The name of the column in the database.
     * @param nativeType
     *            The native column type.
     * @return A column builder.
     */
    public CreateColumn add(String name, String columnName, Class<?> nativeType) {
        return add(name, columnName, Column.getColumnType(nativeType));
    }

    /**
     * Define a new property in the entity with the given name and a
     * <code>java.sql.Types</code> column type appropriate for the given Java
     * native type.
     * 
     * @param name
     *            The property name.
     * @param nativeType
     *            The native column type.
     * @return A column builder.
     */
    public CreateColumn add(String name, Class<?> nativeType) {
        return add(name, name, nativeType);
    }

    /**
     * Define the primary key of the table.
     * 
     * @param columns
     *            The primary key column names.
     * @return This builder to continue building.
     */
    public DefineEntity primaryKey(String... columns) {
        if (!entity.primaryKey.isEmpty()) {
            throw new AddendumException(0);
        }
        entity.primaryKey.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Called when the {@link #end() end} method is called to perform any 
     * actions based on this entity definition.
     */
    protected void ending() {
    }

    /**
     * Terminate the entity definition and the parent addendum builder.
     * 
     * @return The addendum builder.
     */
    public Addendum end() {
        ending();
        return addendum;
    }
}