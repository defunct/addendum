package com.goodworkalan.addendum.jpa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Post {
    /** Test ignoring static fields. */
    public static int STATIC = 1;
    
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
    
    @Column(nullable = true)
    public BigDecimal price;
    
    /** The count of nothing. Test a transient field. */
    @Transient
    public int count;

    /**
     * Set the body. Test a property with a setter but no getter.
     * 
     * @param body
     *            The body.
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    /**
     * Get the price. Test a property with a getter but no setter.
     *  
     * @return The price.
     */
    public BigDecimal getPrice() {
        return price;
    }
}
