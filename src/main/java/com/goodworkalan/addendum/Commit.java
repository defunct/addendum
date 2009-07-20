package com.goodworkalan.addendum;

/**
 * An commit element in the domain-specific language that prevents further
 * addendum definition statements after it has been called.
 * 
 * @author Alan Gutierrez
 */
public interface Commit
{
    /**
     * Terminates the addendum specification statement in the domain-specific
     * language.
     */
    public void commit();
}
