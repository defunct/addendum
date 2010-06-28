package com.goodworkalan.addendum.jdbc;

/**
 * A structure to define a column.
 *
 * @author Alan Gutierrez
 */
public class Column {
    /** The column name. */
    public String name;
    
    /** The column type. */
    public int type;
    
    /** The column size. */
    public int size;
    
    /** The column scale. */
    public int scale;
    
    /** Whether the column can be null. */
    public boolean nullable;
    
    /** Whether the column is auto incremented. */
    public boolean autoIncrement;
}
