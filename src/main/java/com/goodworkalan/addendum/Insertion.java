package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * An update action that inserts a record into the database.
 * 
 * @author Alan Gutierrez
 */
class Insertion implements UpdateSchema
{
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
    public Insertion(String table)
    {
        this.table = table;
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<String>();
    }
    
    /**
     * Set the insert statement columns.
     * 
     * @param cols The column names in the insert statement.
     */
    public void columns(String[] cols)
    {
        for (String column : cols)
        {
            columns.add(column);
        }
    }

    /**
     * Set the insert statement values.
     * 
     * @param vals
     *            The column values in the insert statement.
     * @exception AddendumException
     *                If the count of column values does not match the count of
     *                names.
     */
    public void values(String[] vals)
    {
        if (vals.length != columns.size())
        {
            throw new AddendumException(AddendumException.INSERT_VALUES);
        }
        for (String value : vals)
        {
            values.add(value);
        }
    }

    /**
     * Insert the record described by this insertion statement into the database
     * at the given connection with the given database dialect.
     * 
     * @param database
     *            The psuedo-database.
     */
    public DatabaseUpdate execute(Schema database) {
        return new DatabaseUpdate(0) {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                dialect.insert(connection, table, columns, values);
            }
        };
    }

}
