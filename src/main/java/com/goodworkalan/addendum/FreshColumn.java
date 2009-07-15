package com.goodworkalan.addendum;

public abstract class FreshColumn<Container, Builder> extends DefineColumn<Container, Builder>
{
    public FreshColumn(Container container, String name, int columnType)
    {
        super(container, name, columnType);
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
        setGeneratorType(generatorType);
        return getBuilder();
    }
    
    /**
     * Set the column to not null.
     * 
     * @return This column builder to continue construction.
     */
    public Builder notNull()
    {
        setNotNull(true);
        return getBuilder();
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
        setDefaultValue(defaultValue);
        return getBuilder();
    }
}
