package com.goodworkalan.addendum.jpa;

import javax.persistence.Id;

/**
 * A class where a property setter is annotated with a JPA id annotation.
 * 
 * @author Alan Gutierrez
 */
public class SetterId {
    /** The id. */
    public int id;

    /**
     * Get the id.
     * 
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id.
     * 
     * @param id
     *            The id.
     */
    @Id
    public void setId(int id) {
        this.id = id;
    }
}
