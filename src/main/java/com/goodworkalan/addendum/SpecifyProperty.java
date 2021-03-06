package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

/**
 * A generic column builder that specifies precision, scale length and default
 * value. This builder sets the attributes common to all properties. Derived
 * classes differ based on whether the column is created or altered.
 * 
 * @param <Container>
 *            The type of the containing domain-specific language element.
 * @param <Self>
 *            The type of the subclassed column specification language element.
 * 
 * @author Alan Gutierrez
 */
public abstract class SpecifyProperty<Container, Self> {
    /** The containing domain-specific language element. */
    private Container container;
    
    /** The column definition bean. */
    protected final Column column;

    /**
     * Create a column specifier with the given name and given column type.
     * 
     * @param container
     *            The containing language element.
     * @param column
     *            The column definition bean.
     */
    SpecifyProperty(Container container, Column column) {
        this.container = container;
        this.column = column;
    }

    /**
     * Return this object, but cast to the type needed for the chained
     * invocations of a dependent type. Descendant classes implement this method
     * and simply return the <code>this</code> object. This is necessary to make
     * the return types of the chained builder statements generic.
     * <p>
     * We cannot simply return this, because calling one of the methods in this
     * class will change the type of builder in the build statement. We always
     * want to return the most derived builder type.
     * 
     * @return This builder.
     */
    protected abstract Self getSelf();

    /**
     * Set the database column length to the given length.
     * 
     * @param length
     *            The column length.
     * @return This property builder to continue construction.
     */
    public Self length(int length) {
        column.setLength(length);
        return getSelf();
    }

    /**
     * Set the database column precision to the given precision.
     * 
     * @param precision
     *            The column precision.
     * @return This property builder to continue construction.
     */
    public Self precision(int precision) {
        column.setPrecision(precision);
        return getSelf();
    }

    /**
     * Set the database column scale to the given scale.
     * 
     * @param scale
     *            The column scale.
     * @return This property builder to continue construction.
     */
    public Self scale(int scale) {
        column.setScale(scale);
        return getSelf();
    }

    /**
     * Set the default column value of the database column.
     * 
     * @param defaultValue
     *            The default column value.
     * @return This property builder to continue construction.
     */
    public Self defaultValue(Object defaultValue) {
        column.setDefaultValue(defaultValue);
        return getSelf();
    }

    /**
     * Called at statement termination so that derived classes can create a
     * schema update based on the column defined by this builder.
     */
    protected void ending() {
    }

    /**
     * Terminate the column definition and return the parent entity builder.
     * 
     * @return The parent entity builder.
     */
    public Container end() {
        ending();
        return container;
    }
}
