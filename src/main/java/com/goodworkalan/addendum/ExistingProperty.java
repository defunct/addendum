package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

/**
 * A builder for properties that already exists that can change the type or name
 * of a property and that insists that a default value is provided if the
 * property is made not null.
 * 
 * @author Alan Guteierrez
 * 
 * @param <Container>
 *            The parent builder.
 * @param <Self>
 *            The type of the sub-classed proeprty builder.
 */
public abstract class ExistingProperty<Container, Self>
extends SpecifyProperty<Container, Self> {
    /**
     * Create an alter property element that defines the given column and
     * returns the given container element.
     * 
     * @param container
     *            The container element.
     * @param column
     *            The column definition.
     */
    public ExistingProperty(Container container, Column column) {
        super(container, column);
    }

    /**
     * Set the property type to the given column type according to
     * <code>java.sql.Types</code>.
     * 
     * @param columnType
     *            The column type.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public Self type(int columnType) {
        column.setColumnType(columnType);
        return getSelf();
    }

    /**
     * Set the property type to the given to the <code>java.sql.Types</code>
     * column type mapped to the given native type.
     * 
     * @param nativeType
     *            The native column type.
     * @return This alter property element to continue the domain-specific
     *         language statement.
     */
    public Self type(Class<?> nativeType) {
        column.setColumnType(nativeType);
        return getSelf();
    }

    /**
     * Set the column not null and initialize all existing values with the given
     * default value.
     * 
     * @param defaultValue
     *            The default not null value.
     * @return This property builder to continue construction.
     */
    public Self notNull(Object defaultValue) {
        column.setNotNull(true);
        column.setDefaultValue(defaultValue);
        return getSelf();
    }

    /**
     * Allow the property to be null.
     * 
     * @return This property builder to continue construction.
     */
    public Self nullable() {
        column.setNotNull(false);
        return getSelf();
    }
}
