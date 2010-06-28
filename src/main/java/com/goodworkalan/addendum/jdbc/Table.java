package com.goodworkalan.addendum.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * A structure to gather a table definition.
 *
 * @author Alan Gutierrez
 */
public class Table {
    /** The table name. */
    public String name;
    
    /** The columns. */
    public List<Column> columns = new ArrayList<Column>();
    
    /** The primary key column names. */
    public List<String> primaryKey = new ArrayList<String>();
}
