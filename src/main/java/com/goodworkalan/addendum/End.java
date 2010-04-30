package com.goodworkalan.addendum;

/**
 * An end element for statements that cannot be continued. The insert statement
 * is followed by a columns specification then a values specification then the
 * statement ends. The values specification returns this class since the only
 * acceptable method after values is end.
 * 
 * @author Alan Gutierrez
 */
public class End {
    /** The root language element for schema definition. */
    private final Addendum addendum;

    /**
     * Create an end token.
     * 
     * @param schema
     *            The root language element for schema definition.
     */
    End(Addendum schema) {
        this.addendum = schema;
    }

    /**
     * Terminate the statement and return the schema.
     * 
     * @return The schema.
     */
    public Addendum end() {
        return addendum;
    }
}
