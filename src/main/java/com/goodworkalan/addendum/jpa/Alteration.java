package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Alter;

/**
 * Defines an alteration on the database using an alter element from the
 * addendum domain-specific language.
 * 
 * @author Alan Gutierrez
 */
interface Alteration
{
    /**
     * Define an alteration on the database.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter);
}
