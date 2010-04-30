package com.goodworkalan.addendum.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "un_identified")
public class UnIdentified {
    /** The name. */
    public String name;
}
