package com.goodworkalan.addendum;

/**
 * A builder that sets the values of an insert statement.
 * 
 * @author Alan Gutierrez
 */
public class Values {
    /** The addendum builder. */
    private final Addendum addendum;

    /** The update action that will insert the record. */
    private final Insertion insertion;

    /**
     * Create a values element that will specify the values for the given
     * insertion. The end method of the insert statement will return the given
     * schema.
     * 
     * @param addendum
     *           The addendum builder.
     * @param insertion
     *            The update action that will insert the record.
     */
    Values(Addendum addendum, Insertion insertion) {
        this.addendum = addendum;
        this.insertion = insertion;
    }

    /**
     * Set the insert statement values.
     * 
     * @param values
     *            The column values in the insert statement.
     * @return An end element with which to end the statement.
     * @exception AddendumException
     *                If the count of column values does not match the count of
     *                names.
     */
    public Addendum values(String... values) {
        insertion.values(values);
        return addendum;
    }
}
