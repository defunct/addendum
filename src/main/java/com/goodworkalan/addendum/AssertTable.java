package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Begin an assert table statement to specify column assertions
 * on a table in the database.
 * 
 * @author Alan Gutierrez
 */
public class AssertTable
{
    /**
     * The domain-specific language element that defines a single database
     * migration.
     */
    private final Addendum addendum;

    /**
     * A map of table definitions by table name, used to amend table and column
     * names for the rename form methods.
     */
    private final Map<String, Table> tables;
    
    /** A list of column definitions to verify. */
    private final List<Column> columns;
    
    /** The name of the table to verify. */
    private final String tableName;

    /**
     * Create an alter table statement for the domain-specific langauge.
     * 
     * @param addendum
     *            The domain-specific language element that defines a single
     *            database migration.
     * @param tables
     *            A map of table definitions by table name, used to amend table
     *            and column names for the rename form methods.
     * @param tableName
     *            The name of the table to verify.
     */
    public AssertTable(Addendum addendum, Map<String, Table> tables, String tableName)
    {
        this.addendum = addendum;
        this.tables = tables;
        this.columns = new ArrayList<Column>();
        this.tableName = tableName;
    }
    
    /**
     * Verify that a column in the table exists and matches a specific column
     * definition. If the column does not exist in the table or that column's
     * properties do not match the properties specified in the verify column
     * statement, an exception is raised at update.
     * <p>
     * Those properties that are unspecified by the verify column statement,
     * using the {@link AssertColumn} element are untested and ignored.
     * 
     * @param name
     *            The column name.
     * @param nativeType
     * @return A {@link AssertColumn} element to specify properties to verify.
     */
    public AssertColumn verifyColumn(String name)
    {
        Column column = new Column(name);
        tables.get(tableName).getVerifications().add(Collections.singletonMap(name, column));
        columns.add(column);
        return new AssertColumn(this, column);
    }

    /**
     * Terminate the assert table statement and return the assert interface of
     * the addendum to allow further assertions or population statements.
     * 
     * @return The assert parent element.
     */
    public Assert end()
    {
        return addendum;
    }
}
