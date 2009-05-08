package com.goodworkalan.addendum;

/**
 * Represents and end method for statements that cannot be continued. The insert
 * statement is followed by a columns specification then a values specification
 * then the statement ends. The values specification returns this class since
 * the only acceptable method after values is end.
 *  
 * @author Alan Gutierrez
 */
public class End
{
    /** The root language element for schema definition. */
    private final Schema schema;

    /**
     * Create an end token.
     * 
     * @param schema
     *            The root language element for schema definition.
     */
    End(Schema schema)
    {
        this.schema = schema;
    }
    
    /**
     * Terminate the statement and return the schema.
     * 
     * @return The schema.
     */
    public Schema end()
    {
        return schema;
    }
}
