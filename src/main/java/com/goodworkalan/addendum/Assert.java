package com.goodworkalan.addendum;

/**
 * An assert element in the domain specific language that prevents the creation
 * of new tables or alteration of existing tables in an addendum after
 * assertions have been defined.
 * 
 * @author Alan Gutierrez
 */
public interface Assert extends Populate
{
    /**
     * Assert that the table definition for the given table name matches an
     * expected table definition.
     * 
     * @param name
     *            The table name.
     * @return An assert table element to define the expected table definition.
     */
    public AssertTable assertTable(String name);
}
