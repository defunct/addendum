package com.goodworkalan.addendum;

/**
 * Builds an entity alteration that can change the underlying table name, alter
 * properties, add new properties or drop properties.
 * 
 * @author Alan Gutierrez
 */
public class AlterEntity {
    /** The addendum builder to return when this builder terminates. */
    private final Addendum addendum;

    /** The database migration script. */
    private final Script script;
    
    /** The entity to alter. */
    private final Entity entity;

    /**
     * Create an alter table builder that alters the given entity.
     * 
     * @param addendum
     *            The addendum builder to return when this builder terminates.
     * @param entity
     *            The entity to alter.
     * @param script
     *            The database migration script.
     */
    AlterEntity(Addendum addendum, Entity entity, Script script) {
        this.addendum = addendum;
        this.entity = entity;
        this.script = script;
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
        return new RenameProperty(this, script, entity.tableName, column, from);
    }

    /**
     * Add a new column to the table with the given name and given column type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The <code>java.sql.Type</code> column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, int columnType) {
        if (entity.properties.containsKey(name)) {
            throw new AddendumException(0, name);
        }
        Column column = new Column(name, columnType);
        return new AddProperty(this, script, entity.tableName, name, column);
    }
    
    /**
     * Add a new column to the table with the given name and a column type
     * appropriate for the given Java primitive.
     * 
     * @param name
     *            The column name.
     * @param nativeType
     *            The native column type.
     * @return An add column language element to define the column.
     */
    public AddProperty add(String name, Class<?> nativeType) {
        if (entity.properties.containsKey(name)) {
            throw new AddendumException(0, name);
        }
        Column column = new Column(name, nativeType);
        return new AddProperty(this, script, entity.tableName, name, column);
    }

    /**
     * Begin an alter column statement to alter the column with the given name.
     * 
     * @param property
     *            The name of the property to alter.
     * @return An alter column language element to define the column changes.
     */
    public AlterColumn alter(String property) {
        Column column = new Column(entity.getColumn(property));
        return new AlterColumn(this, script, entity.tableName, column);
    }
    
    
    public AlterEntity drop(String property) {
        script.add(new ColumnDrop(entity.tableName, property));
        return this;
    }

    public Addendum end () {
        return addendum;
    }
}
