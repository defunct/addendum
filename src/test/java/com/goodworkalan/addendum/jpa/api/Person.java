package com.goodworkalan.addendum.jpa.api;

import javax.persistence.Entity;

@Entity
public class Person
{
    private int hatSize;
    
    public int getHatSize()
    {
        return hatSize;
    }
    
    public void setHatSize(int hatSize)
    {
        this.hatSize = hatSize;
    }
}
