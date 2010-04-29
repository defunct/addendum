package com.goodworkalan.addendum;


/**
 * Performs an update defined by the domains-specific language used by
 * {@link DatabaseAddendum} on a database connection using a dialect.
 * 
 * @author Alan Gutierrez
 */
interface Update {
    public UpdateDatabase execute(Schema schema);
}
