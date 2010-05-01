package com.goodworkalan.addendum.jpa.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Trackback {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(nullable = true, name = "post_id")
    public Post post;
}
