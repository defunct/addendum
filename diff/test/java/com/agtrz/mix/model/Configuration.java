/* Copyright Alan Gutierrez 2006 */
package com.agtrz.mix.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Configuration
{
    private long id;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */