package com.goodworkalan.addendum.jpa;

import javax.persistence.Id;

/**
 * A class where a property getter is annotated with a JPA id annotation.
 * 
 * @author Alan Gutierrez
 */
public class GetterId {
    /** The id. */
    public int id;

    /**
     * Get the id.
     * 
     * @return The id.
     */
    @Id
    public int getId() {
        return id;
    }

    /**
     * Set the id.
     * 
     * @param id
     *            The id.
     */
    public void setId(int id) {
        this.id = id;
    }
}
