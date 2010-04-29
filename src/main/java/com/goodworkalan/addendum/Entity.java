package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A mutable object that models a table specification.
 * 
 * @author Alan Gutierrez
 */
public class Entity
{
    /** The table name. */
    public String tableName;
    
    /** The primary key. */
    private List<String> primaryKey;
    
    /** The map of columns by column name. */
    private Map<String, Column> columns = new LinkedHashMap<String, Column>();
    
    /** The map of verifications to perform on the table. */
    private List<Map<String, Column>> verifications = new ArrayList<Map<String, Column>>();

    /**
     * Create a table with given table name.
     * 
     * @param name
     *            The table name.
     */
    public Entity(String name) {
        this.primaryKey = new ArrayList<String>();
        this.tableName = name;
    }

    /**
     * Return the primary key of the table as a list of primary key column
     * names.
     * 
     * @return The primary key.
     */
    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Get the map of columns by column name.
     * 
     * @return The map of columns by column name.
     */
    public Map<String, Column> getColumns()
    {
        return columns;
    }
    
    /**
     * Get the map of verifications to perform on the table.
     * 
     * @return The map of verifications to perform on the table.
     */
    public List<Map<String, Column>> getVerifications()
    {
        return verifications;
    }
}
