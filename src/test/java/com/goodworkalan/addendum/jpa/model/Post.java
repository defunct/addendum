package com.goodworkalan.addendum.jpa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorColumn(name = "post_type")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    
    @OneToMany(mappedBy = "comment")
    public List<Comment> comments;

    @Column(length = 128)
    public String summary;
    
    @Column(nullable = false, name = "created_at")
    public Date createdAt;
    
    @Lob @Column(nullable = false)
    public String body;
}
