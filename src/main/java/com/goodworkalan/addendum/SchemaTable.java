package com.goodworkalan.addendum;

import java.util.List;

/**
 * Element for a verify table statement begun by
 * {@link Schema#verifyTable(String)} to specify a table verification performed
 * after all addenda are applied.
 * 
 * @author Alan Gutierrez
 * 
 */
public class SchemaTable
{
    /** The root schema verification element. */
    private final Schema schema;
    
    /** The table definition. */
    private final Table table;
    
    /** A list of verifications to perform after all addenda are complete. */
    private final List<Update> updates;
    
    /**
     * Create a schema table verification element that adds columns to the given
     * list of columns.
     * 
     * @param schema
     *            The root schema verification element.
     * @param updates
     *            A list of verifications to perform after all addenda are
     *            complete.
     * @param tableName
     *            The table name.
     */
    SchemaTable(Schema schema, List<Update> updates, String tableName)
    {
        this.schema = schema;
        this.updates = updates;
        this.table = new Table(tableName);
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
     * @return A {@link SchemaColumn} element to specify properties to verify.
     */
    public SchemaColumn column(String name)
    {
        Column column = new Column(name);
        table.getColumns().put(name, column);
        return new SchemaColumn(this, column);
    }

    /**
     * Terminate the verify table statement and return the schema root element
     * to allow further verifications.
     * 
     * @return The schema parent element.
     */
    public Schema end()
    {        
        updates.add(new TableVerification(table));
        return schema;
    }
}