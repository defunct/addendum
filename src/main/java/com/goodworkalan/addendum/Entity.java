package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A mutable object that models a table specification.
 * 
 * @author Alan Gutierrez
 */
public class Entity {
   /** The table name. */
    public String tableName;

    /**
     * The primary key.
     * <p>
     * FIXME The primary key is properties or columns?
     */
    public final List<String> primaryKey = new ArrayList<String>();
    
    /** The map of property mappings. */
    public Map<String, String> properties = new HashMap<String, String>();

    /** The map of properties by column name. */
    public Map<String, Column> columns = new LinkedHashMap<String, Column>();

    /**
     * Create a table with given table name.
     * 
     * @param tableName
     *            The table name.
     */
    public Entity(String tableName) {
        this.tableName = tableName;
    }
    
    public Column getColumn(String property) {
        String columnName = properties.get(property);
        if (columnName == null) {
            throw new AddendumException(0, property);
        }
        Column column = columns.get(columnName);
        if (column == null) {
            throw new AddendumException(0, property, columnName);
        }
        return column;
    }
}
