package com.goodworkalan.addendum.dialect;

import java.util.Collection;
import java.util.List;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Record of a table creation.
 *
 * @author Alan Gutierrez
 */
public class CreateTable {
    /** The table name. */
    public final String tableName;
    
    /** The column definitions. */
    public final Collection<Column> columns;
    
    /** The primary key. */
    public final List<String> primaryKey;

    /**
     * Create a table creation record.
     * 
     * @param tableName
     *            The table name.
     * @param columns
     *            The column definitions.
     * @param primaryKey
     *            The primary key.
     */
    public CreateTable(String tableName, Collection<Column> columns, List<String> primaryKey) {
        this.tableName = tableName;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }
}
