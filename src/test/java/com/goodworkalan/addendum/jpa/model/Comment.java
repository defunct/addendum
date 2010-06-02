package com.goodworkalan.addendum.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.testng.annotations.Factory;

public class Comment {
    /** The id. Test ignoring a pointless lob. */
    @Lob
    public long id;
    
    private Post post;
    
    private Date createdAt;
    
    public int body;
    
    @OneToOne(mappedBy = "post")
    public Archive archive;
    
    private String whatWasSaid;
    
    @Id
    @Factory
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    @ManyToOne
    @JoinColumn(nullable = false)
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    @Column(nullable = false, name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    
    public String getBody() {
        return whatWasSaid;
    }
    
    @Lob @Column(nullable = false)
    public void setBody(String body) {
        this.whatWasSaid = body;
    }
}