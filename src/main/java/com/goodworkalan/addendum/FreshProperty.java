package com.goodworkalan.addendum;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Base builder for property definition builders for properties added to
 * entities.
 * 
 * @author Alan Gutierrez
 * 
 * @param <Container>
 *            The parent builder in the domain-specific language.
 * @param <Self>
 *            The type of the sub-classed property builder.
 */
public abstract class FreshProperty<Container, Self>
extends SpecifyProperty<Container, Self> {
    /**
     * Create a property builder with the given container builder that builds
     * the given column definition.
     * 
     * 
     * @param container
     *            The containing builder.
     * @param column
     *            The column definition.
     */
    public FreshProperty(Container container, Column column) {
        super(container, column);
    }

    /**
     * Set the unique id generator type of the database column.
     * 
     * @param generatorType
     *            The unique id generator type.
     * @return This property builder to continue construction.
     */
    public Self generator(GeneratorType generatorType) {
        column.setGeneratorType(generatorType);
        return getSelf();
    }
    
    /**
     * Set the database column to not null.
     * 
     * @return This property builder to continue construction.
     */
    public Self notNull() {
        column.setNotNull(true);
        return getSelf();
    }
}
