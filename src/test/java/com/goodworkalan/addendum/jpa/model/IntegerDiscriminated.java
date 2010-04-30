package com.goodworkalan.addendum.jpa.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An entity whose type is discriminated by an integer.
 *
 * @author Alan Gutierrez
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class IntegerDiscriminated {
    /** The id. */
    @Id
    public int id;
}
