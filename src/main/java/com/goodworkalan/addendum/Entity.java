package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.COLUMN_MISSING;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_MISSING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.addendum.dialect.Column;

/**
 * A mutable object that models a table specification.
 * 
 * @author Alan Gutierrez
 */
class Entity {
   /** The table name. */
    public String tableName;

    /**
     * The primary key columns. Using properties is too clever. It is not as if
     * the person defining the database is unaware of its underlying structure.
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

    /**
     * Get the column definition for the given property name.
     * 
     * @param property
     *            The property name.
     * @return The column definition.
     * @exception AddendumException
     *                If the property cannot be found or if the column cannot be
     *                found.
     */
    public Column getColumn(String property) {
        String columnName = properties.get(property);
        if (columnName == null) {
            throw new AddendumException(PROPERTY_MISSING, property);
        }
        Column column = columns.get(columnName);
        if (column == null) {
            throw new AddendumException(COLUMN_MISSING, columnName);
        }
        return column;
    }

    /**
     * Get the property name associated with the given column name or throw an
     * illegal argument exception of the column name cannot be found.
     * 
     * @param columnName
     *            The column name.
     * @return The property name.
     * @exception IllegalArgumentException
     *                If the argument cannot be found.
     */
    public String getPropertyName(String columnName) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getValue().equals(columnName)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Rename the property to with the name given by from to the name given by
     * to. Throws an exception if the property does not exist in the entity.
     * 
     * @param from
     *            The name to rename from.
     * @param to
     *            The name to rename to.
     * @exception AddendumException
     *                If the property does not exist in this entity.
     */
    public void rename(String from, String to) {
        if (properties.containsKey(to)) {
            throw new AddendumException(PROPERTY_EXISTS, to);
        }
        properties.put(to, properties.remove(from));
    }
}
