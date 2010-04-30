package com.goodworkalan.addendum;

/**
 * A property builder for properties added to existing entities that defines the
 * type, column name, length, precision, scale, nullability and default value of
 * the property.
 * <p>
 * This class is used in the context of a domain-specific language that is
 * implemented as chained Java method calls. Refer to the package documentation
 * for documentation of the language and examples of use.
 * 
 * @author Alan Gutierrez
 */
public class AddProperty extends FreshProperty<AlterTable, AddProperty> {
    /** The database migration script. */
    private final Script script;

    /** The name of the table in which to add the column. */
    private final String tableName;
    
    /** The name of the new property. */
    private final String property;

    /**
     * Create a new add property builder that will add an alter column update
     * based on the given column to the given migration script. The update
     * statement will add a column to the given script. that will return the
     * given alter table builder when it terminates.
     * 
     * @param container
     *            The parent alter table builder.
     * @param script
     *            The database migration script.
     * @param tableName
     *            The name of the table in which to add the column.
     * @param property
     *            The name of the new property.
     * @param column
     *            The column definition.
     */
    AddProperty(AlterTable container, Script script, String tableName, String property, Column column) {
        super(container, column);
        this.script = script;
        this.property = property;
        this.tableName = tableName;
    }
    
    /**
     * Return this builder used to continue construction.
     * 
     * @return This builder.
     */
    @Override
    protected AddProperty getElement() {
        return this;
    }

    /**
     * Overridden to add an add column update to the migration script when
     * the builder terminates. 
     */
    @Override
    protected void ending() {
        script.add(new TableAlteration(tableName, property, column));
    }
}
