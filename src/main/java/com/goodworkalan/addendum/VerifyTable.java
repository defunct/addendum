package com.goodworkalan.addendum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Begin an assert table statement to specify column assertions on a table in
 * the database.
 * 
 * @author Alan Gutierrez
 */
public class VerifyTable
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

    /**
     * The list of updates for the addendum that defines this verify table
     * statement.
     */
    private final List<Update> updates;

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
    public VerifyTable(Addendum addendum, List<Update> updates, Map<String, Table> tables, String tableName)
    {
        this.addendum = addendum;
        this.updates = updates;
        this.tables = tables;
        this.tableName = tableName;
    }

    /**
     * Verify that a column in the table exists and matches a specific column
     * definition. If the column does not exist in the table or that column's
     * properties do not match the properties specified in the verify column
     * statement, an exception is raised at update.
     * <p>
     * Those properties that are unspecified by the verify column statement,
     * using the {@link VerifyColumn} element are untested and ignored.
     * 
     * @param name
     *            The column name.
     * @return A {@link VerifyColumn} element to specify properties to verify.
     */
    public VerifyColumn verifyColumn(String name)
    {
        Column column = new Column(name);
        tables.get(tableName).getVerifications().add(Collections.singletonMap(name, column));
        return new VerifyColumn(this, column);
    }

    /**
     * Terminate the verify table statement and return the verify interface of
     * the addendum to allow further verifications or population statements.
     * 
     * @return The assert parent element.
     */
    public Verify end()
    {
        updates.add(new TableVerification(tables.get(tableName)));
        return addendum;
    }
}
