package com.goodworkalan.addendum.jpa.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Not sure what it would really be, but it is one to one with post.
 *
 * @author Alan Gutierrez
 */
public class Archive {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(optional = false, targetEntity = Post.class)
    public Post post;
}
