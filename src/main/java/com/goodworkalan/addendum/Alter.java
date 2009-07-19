package com.goodworkalan.addendum;

/**
 * An alter element in the domain specific language that prevents the creation
 * of new tables in an addendum after alterations have been defined.
 * 
 * @author Alan Gutierrez
 */
public interface Alter extends Assert
{
    /**
     * Alter an existing table with the given name.
     * 
     * @param name
     *            The table name.
     * @return An alter table element to specifiy changes to the table.
     */
    public AlterTable alterTable(String name);
}
