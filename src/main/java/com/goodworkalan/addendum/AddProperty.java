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
public class AddProperty extends FreshProperty<AlterEntity, AddProperty> {
    /** The database migration patch. */
    private final Patch patch;

    /** The name of the table in which to add the column. */
    private final String tableName;
    
    /** The name of the new property. */
    private final String property;

    /**
     * Create a new add property builder that will add an alter column update
     * based on the given column to the given migration patch. The update
     * statement will add a column to the given patch. that will return the
     * given alter table builder when it terminates.
     * 
     * @param container
     *            The parent alter table builder.
     * @param patch
     *            The database migration patch.
     * @param tableName
     *            The name of the table in which to add the column.
     * @param property
     *            The name of the new property.
     * @param column
     *            The column definition.
     */
    AddProperty(AlterEntity container, Patch patch, String tableName, String property, Column column) {
        super(container, column);
        this.patch = patch;
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
     * Overridden to add an add column update to the migration patch when
     * the builder terminates. 
     */
    @Override
    protected void ending() {
        patch.add(new ColumnAdd(tableName, property, column));
    }
}
