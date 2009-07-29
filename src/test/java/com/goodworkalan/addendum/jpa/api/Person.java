package com.goodworkalan.addendum.jpa.api;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person
{
    private int id;
    
    private String firstName;
    
    private String lastName;
    
    @Id
    public int getId()
    {
        return id;
    }
    
    public void setId(int hatSize)
    {
        this.id = hatSize;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
