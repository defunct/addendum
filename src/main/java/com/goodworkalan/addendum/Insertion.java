package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.INSERT_VALUES;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.danger.Danger;

/**
 * An update action that inserts a record into the database.
 * 
 * @author Alan Gutierrez
 */
class Insertion implements SchemaUpdate {
    /** The table name. */
    private final String table;

    /** The column names in the insert statement. */
    private final List<String> columns;

    /** The column values in the insert statement. */
    private final List<String> values;

    /**
     * Create an insertion update action that will insert into the table with
     * the given table name.
     * 
     * @param table
     *            The table name.
     */
    public Insertion(String table) {
        this.table = table;
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<String>();
    }

    /**
     * Set the insert statement columns.
     * 
     * @param cols
     *            The column names in the insert statement.
     */
    public void columns(String[] cols) {
        for (String column : cols) {
            columns.add(column);
        }
    }

    /**
     * Set the insert statement values.
     * 
     * @param vals
     *            The column values in the insert statement.
     * @exception Addendum
     *                If the count of column values does not match the count of
     *                names.
     */
    public void values(String[] vals) {
        if (vals.length != columns.size()) {
            throw new Danger(Addendum.class, INSERT_VALUES);
        }
        for (String value : vals) {
            values.add(value);
        }
    }

    /**
     * Insert the record described by this insertion statement into the database
     * at the given connection with the given database dialect.
     * 
     * @param schema
     *            The tracking schema.
     */
    public DatabaseUpdate execute(Schema database) {
        return new DatabaseUpdate(INSERT_VALUES, table) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.insert(connection, table, columns, values);
            }
        };
    }

}
