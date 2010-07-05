package com.goodworkalan.addendum.dialect;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Record of column alteration.
 *
 * @author Alan Gutierrez
 */
public class AlterColumn {
    /** The table name. */
    public final String tableName;

    /** The old column name. */
    public final String oldName;

    /** The column definition. */
    public final Column column;

    /**
     * Create a column alteration record.
     * 
     * @param tableName
     *            The table name.
     * @param oldName
     *            The old column name.
     * @param column
     *            The column definition.
     */
    public AlterColumn(String tableName, String oldName, Column column) {
        this.tableName = tableName;
        this.oldName = oldName;
        this.column = column;
    }
}
