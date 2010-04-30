package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Builds a new property for an entity specifying the type, length, scale,
 * precision, nullability, and default value.
 * <p>
 * This class is used in the context of a domain-specific language that is
 * implemented as chained Java method calls. Refer to the package documentation
 * for documentation of the language and examples of use.
 * 
 * @author Alan Gutierrez
 */
public class CreateProperty extends FreshProperty<DefineEntity, CreateProperty> {
    /**
     * Build the given column for new property in the given entity builder.
     * 
     * @param table
     *            The entity builder.
     * @param column
     *            The column definition.
     */
    public CreateProperty(DefineEntity table, Column column) {
        super(table, column);
    }

    /**
     * Return this builder used to continue construction.
     * 
     * @return This builder.
     */
    @Override
    protected CreateProperty getElement() {
        return this;
    }
}
