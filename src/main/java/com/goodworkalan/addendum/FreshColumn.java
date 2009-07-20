package com.goodworkalan.addendum;

/**
 * Base element for column definition elements for columns that do not yet
 * exist.
 * 
 * @author Alan Guteierrez
 * 
 * @param <Container>
 *            The parent element in the domain-specific language.
 * @param <Element>
 *            The type of the sub-classed column element.
 */
public abstract class FreshColumn<Container, Builder> extends DefineColumn<Container, Builder>
{
    /**
     * Create a column specifier with the given name and given column type.
     * 
     * @param container
     *            The containing language element.
     * @param column
     *            The column definition bean.
     */
    public FreshColumn(Container container, Column column)
    {
        super(container, column);
    }

    /**
     * Set the unique id generator type;
     * 
     * @param generatorType
     *            The unique id generator type.
     * @return This column builder to continue construction.
     */
    public Builder generator(GeneratorType generatorType)
    {
        column.setGeneratorType(generatorType);
        return getElement();
    }
    
    /**
     * Set the column to not null.
     * 
     * @return This column builder to continue construction.
     */
    public Builder notNull()
    {
        column.setNotNull(true);
        return getElement();
    }

    /**
     * Set the default column value.
     * 
     * @param defaultValue
     *            The default column value.
     * @return This column builder to continue construction.
     */
    public Builder defaultValue(String defaultValue)
    {
        column.setDefaultValue(defaultValue);
        return getElement();
    }
}
