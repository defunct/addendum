package com.goodworkalan.addendum.dialect;


/**
 * Record of an column addition.
 *
 * @author Alan Gutierrez
 */
public class AddColumn {
    /** The table name. */
    public final String tableName;

    /** The column. */
    public final Column column;

    /**
     * Create a column addition record.
     * 
     * @param tableName
     *            The table name.
     * @param column
     *            The column.
     */
    public AddColumn(String tableName, Column column) {
        this.tableName = tableName;
        this.column = column;
    }
}
