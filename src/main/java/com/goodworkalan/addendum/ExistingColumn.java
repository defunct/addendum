package com.goodworkalan.addendum;

/**
 * Base element for column definition elements where the type property is
 * optionally specified.
 * 
 * @author Alan Guteierrez
 * 
 * @param <Container>
 *            The parent element in the domain-specific language.
 * @param <Element>
 *            The type of the sub-classed column element.
 */
public abstract class ExistingColumn<Container, Element> extends DefineColumn<Container, Element>
{
    /**
     * Create an alter column element that defines the given column and returns
     * the given container element.
     * 
     * @param container
     *            The container element.
     * @param column
     *            The column definition.
     */
    public ExistingColumn(Container container, Column column)
    {
        super(container, column);
    }

    /**
     * Set the column type to the given column type according to
     * <code>java.sql.Types</code>.
     * 
     * @param columnType
     *            The column type.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public Element type(int columnType)
    {
        column.setColumnType(columnType);
        return getElement();
    }
    
    /**
     * Set the column type to the given to the <code>java.sql.Types</code>
     * column type mapped to the given native type.
     * 
     * @param nativeType
     *            The native column type.
     * @return This alter column element to continue the domain-specific
     *         language statement.
     */
    public Element type(Class<?> nativeType)
    {
        column.setColumnType(nativeType);
        return getElement();
    }
}
