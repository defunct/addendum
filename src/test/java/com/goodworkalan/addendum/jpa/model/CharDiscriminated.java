package com.goodworkalan.addendum.jpa.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An entity whose type is discriminated by a single character.
 *
 * @author Alan Gutierrez
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.CHAR)
public class CharDiscriminated {
    /** The id. */
    @Id
    public int id;
}
