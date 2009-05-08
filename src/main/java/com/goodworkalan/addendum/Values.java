package com.goodworkalan.addendum;

/**
 * A domain-specific language element to set the values of an insert statement.
 * 
 * @author Alan Gutierrez
 */
public class Values
{
    /** The root language element for schema definitions. */
    private final Schema schema;
    
    /** The update action that will insert the record. */
    private final Insertion insertion;
    
    Values(Schema schema, Insertion insertion)
    {
        this.schema = schema;
        this.insertion = insertion;
    }

    /**
     * Set the insert statement values.
     * 
     * @param vals
     *            The column values in the insert statement.
     * @return An end element with which to end the statement.
     * @exception AddendumException
     *                If the count of column values does not match the count of
     *                names.
     */
    public End values(String... values)
    {
        insertion.values(values);
        return new End(schema);
    }
}
