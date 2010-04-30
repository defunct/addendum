package com.goodworkalan.addendum;


/**
 * Begins an alter table statement in the domain-specific language for defining
 * database migrations.
 * 
 * @author Alan Gutierrez
 */
public class AlterTable {
    /**
     * The domain-specific language element that defines a single database
     * migration.
     */
    private final Addendum addendum;

    private final Script script;
    
    private final Entity entity;
    
    /**
     * Create an alter table statement for the domain-specific language.
     * 
     * @param addendum
     *            The domain-specific language element that defines a single
     *            database migration.
     * @param tableName
     *            The name of the table to alter.
     * @param updates
     *            The list of updates for the addendum that defines this alter
     *            table statement.
     * @param tables
     *            A list of maps of table definitions by table name, one map for
     *            each addendum, used to amend table and column names for the
     *            rename form methods.
     */
    AlterTable(Addendum addendum, Entity entity, Script script) {
        this.addendum = addendum;
        this.entity = entity;
        this.script = script;
    }

    public RenameColumn rename(String from) {
        Column column = new Column(entity.getColumn(from));
        return new RenameColumn(this, script, entity.tableName, column, from);
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
    
    
    public AlterTable drop(String property) {
        script.add(new ColumnDrop(entity.tableName, property));
        return this;
    }

    public Addendum end () {
        return addendum;
    }
}
