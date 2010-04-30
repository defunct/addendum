package com.goodworkalan.addendum.jpa;

import javax.persistence.Id;

/**
 * A class where a field is annotated with a JPA id annotation.
 * 
 * @author Alan Gutierrez
 */
public class FieldId {
    /** The id. */
    @Id
    public int id;
}
