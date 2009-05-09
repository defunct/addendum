package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * An update action that inserts a record into the database.
 * 
 * @author Alan Gutierrez
 */
class Insertion implements Update
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
            columns.add(value);
        }
    }

    public void execute(Connection connection, Dialect dialect) 
        throws SQLException
    {
        String sql = dialect.insert(table, columns, values);

        PreparedStatement statement = connection.prepareStatement(sql);
        
        for (int i = 0; i < values.size(); i++)
        {
            String value = values.get(i);
            if (value == null)
            {
                statement.setNull(i + 1, Types.VARCHAR);
            }
            else
            {
                statement.setString(i + 1, value);
            }
        }
        
        statement.execute();
        statement.close();
    }
}
