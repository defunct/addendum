package com.goodworkalan.addendum;


/**
 * Performs an update against the tracking schema and creates an update
 * to be performed against the database.
 * <p>
 * FIXME Rename SchemaUpdate.
 * 
 * @author Alan Gutierrez
 */
interface UpdateSchema {
    /**
     * Execute an update against the given tracking schema and return an update
     * to be performed against the database.
     * 
     * @param schema
     *            The tracking schema.
     * @return An update to be performed against the database.
     */
    public DatabaseUpdate execute(Schema schema);
}
