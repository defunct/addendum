package com.goodworkalan.addendum;

import java.util.Arrays;

/**
 * Begins a create table statement in the domain-specific language used to
 * define database update actions.
 * 
 * @author Alan Gutierrez
 */
public class TableElement
{
    private final Runnable ending;

    /** The root language element. */
    private final Addendum addendum;
    
    /** The entity definition. */
    private final Entity entity;
    
    /**
     * Create a table builder with the given root language element.
     * 
     * @param schema
     *            The root language element.
     * @param columns
     *            The list of column definitions.
     */
    TableElement(Addendum schema, Entity entity, Runnable ending) {
        this.addendum = schema;
        this.entity = entity;
        this.ending = ending;
    }

    /**
     * Get the column definition for the given name or create one if the column
     * definition does not exist.
     * 
     * @param name
     *            The column name.
     * @return The column definition.
     */
    private Column newColumn(String name) {
        if (entity.getColumns().containsKey(name)) {
            throw new AddendumException(0);
        }
        Column column = new Column(name);
        entity.getColumns().put(name, column);
        return column;
    }
    
    public TableElement table(String name) {
        entity.tableName = name;
        return this;
    }

    /**
     * Define a new column in the table with the given name and given column
     * type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The SQL column type.
     * @return A column builder.
     */
    public CreateColumn add(String name, int columnType) {
        Column column = newColumn(name);
        column.setDefaults(columnType);
        return new CreateColumn(this, column);
    }

    /**
     * Define a new column in the table with the given name and a
     * <code>java.sql.Types</code> column type appropriate for the given native
     * Java type.
     * 
     * @param name
     *            The column name.
     * @param nativeType
     *            The native column type.
     * @return A column builder.
     */
    public CreateColumn add(String name, Class<?> nativeType) {
        Column column = newColumn(name);
        column.setDefaults(nativeType);
        return new CreateColumn(this, column);
    }

    /**
     * Define the primary key of the table.
     * 
     * @param columns
     *            The primary key column names.
     * @return This builder to continue building.
     */
    public TableElement primaryKey(String... columns) {
        entity.getPrimaryKey().addAll(Arrays.asList(columns));
        return this;
    }


    /**
     * Terminate the table definition and return the schema.
     * 
     * @return The schema.
     */
    public Addendum end() {
        ending.run();
        return addendum;
    }
}