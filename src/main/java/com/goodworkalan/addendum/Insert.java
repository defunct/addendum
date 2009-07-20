package com.goodworkalan.addendum;

/**
 * An update action that inserts a record into the database.
 *  
 * @author Alan Gutierrez
 */
public class Insert
{
    /** The root language element for schema definitions. */
    private final Addendum schema;
    
    /** The update action that will insert the record. */
    private final Insertion insertion;

    /**
     * Create a new insert element that will specify the columns for the given
     * insertion. The end method of the insert statement will return the given
     * schema.
     * 
     * @param schema
     *            The root language element for schema definitions.
     * @param insertion
     *            The update action that will insert the record.
     */
    Insert(Addendum schema, Insertion insertion)
    {
        this.schema = schema;
        this.insertion = insertion;
    }

    /**
     * Set the insert statement columns.
     * 
     * @param columns
     *            The column names in the insert statement.
     * @return A values element with which to specify the insert values.
     */
    public Values columns(String... columns)
    {
        insertion.columns(columns);
        return new Values(schema, insertion);
    }
}
