package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Begins an alter table statement in the domain-specific language for defining
 * database migrations.
 * 
 * @author Alan Gutierrez
 */
// FIXME Drop column.
public class AlterTable
{
    /**
     * The domain-specific language element that defines a single database
     * migration.
     */
    private final Addendum addendum;

    /** The name of the table to alter. */
    private String tableName;

    /**
     * The list of updates for the addendum that defines this alter table
     * statement.
     */
    private final List<Update> updates;
    
    /** The list of column definitions of columns to add to the table. */
    private final List<Column> addColumns;
    
    /**
     * A list of maps of table definitions by table name, one map for each
     * addendum, used to amend table and column names for the rename form
     * methods.
     */
    private final LinkedList<Map<String, Table>> tables;

    /**
     * Create an alter table statement for the domain-specific language.
     * 
     * @param addendum
     *            The domain-specific language element that defines a single
     *            database migration.
     * @param tableName
     *            The name of the table to alter.
     * @param updates
     *            The list of updates for the addendum that defines this alter
     *            table statement.
     * @param tables
     *            A list of maps of table definitions by table name, one map for
     *            each addendum, used to amend table and column names for the
     *            rename form methods.
     */
    AlterTable(Addendum addendum, String tableName, List<Update> updates, LinkedList<Map<String, Table>> tables)
    {
        this.addendum = addendum;
        this.tableName = tableName;
        this.updates = updates;
        this.addColumns  = new ArrayList<Column>();
        this.tables = tables;
    }

    /**
     * Rename the table from the current name to the given name.
     * 
     * @param newName
     *            The new table name.
     * @return This alter table element to continue the domain-specific language
     *         statement.
     */
    AlterTable rename(String newName)
    {
        updates.add(new RenameTable(tableName, newName));
        tableName = newName;
        return this;
    }

    /**
     * Rename the table from the given old name to the current name. This method
     * will update all references to the name property of the alter table element
     * in previous addenda to the given old name.
     * <p>
     * This method is used when definitions are driven by a library that uses
     * reflection to introspect Java objects to create table definitions. If the
     * Java object is renamed, and the library is using the Java class name to
     * generate a table name, then any place where the class name is used to
     * define a table name is updated.
     * <p>
     * For example, let's say version one of our application has a class named
     * <code>User</code> and we use introspection to define the table in which
     * users are stored.
     * 
     * <pre>
     * &lt;
     * addenda
     *     .addendum()
     *         .createTable(User.getClass().getName())
     *             .column(&quot;firstName&quot;, String.class).length(64).end()
     *             .column(&quot;lastName&quot;, String.class).length(64).end()
     *             .end()
     *         .commit();
     * </pre>
     * <p>
     * Let's say we decide to be a little less dehumanizing in our data model
     * and we decide to call users people. When we rename the class from
     * <code>User</code> to <code>Person</code>, all references in our addendum
     * are also going to change. This is where we use rename from to indicate
     * what the name of the table once was.
     * 
     * <pre>
     * &lt;
     * addenda
     *     .addendum()
     *         .createTable(Person.getClass().getName())
     *             .column(&quot;firstName&quot;, String.class).length(64).end()
     *             .column(&quot;lastName&quot;, String.class).length(64).end()
     *             .end()
     *         .commit();
     * addenda
     *     .addendum()
     *         .alterTable(Person.getClass().getName())
     *             .renameFrom(&quot;User&quot;)
     *             .end()
     *         .commit();
     * </pre>
     * <p>
     * The <code>renameFrom</code> method will run back through the previous
     * definitions changing references to a table named <code>Person</code> to a
     * table named <code>User</code>. It will add a rename statement to the
     * addendum in which the rename from was defined. Going forward references
     * to <code>Person</code> will generate SQL that updates the
     * <code>Person</code> table, unless a subsequent table is defined.
     * 
     * @param oldName
     *            The table name to rename from.
     * @return This alter table element to continue the domain-specific language
     *         statement.
     */
    public AlterTable renameFrom(String oldName)
    {
        for (Map<String, Table>  map : tables)
        {
            Table table = map.get(tableName);
            if (table != null)
            {
                if (table.getName().equals(tableName))
                {
                    table.setName(oldName);
                }
                else
                {
                    break;
                }
            }
        }
        updates.add(new RenameTable(oldName, tableName));
        return this;
    }

    /**
     * Create a new column for an add column or alter column statement raising
     * an exception if the column has alredy been added or altered.
     * 
     * @param name
     *            The column name.
     * @param errorCode
     *            The error code to raise in an {@link AddendumException} if the
     *            column has already been added or altered.
     * @return A new column definition.
     */
    private Column newColumn(String name, int errorCode)
    {
        Column column = tables.getFirst().get(tableName).getColumns().get(name);
        if (column == null)
        {
            column = new Column(name);
            tables.getFirst().get(tableName).getColumns().put(name, column);
            return column;
        }
        throw new AddendumException(errorCode);
    }

    /**
     * Add a new column to the table with the given name and given column type.
     * 
     * @param name
     *            The column name.
     * @param columnType
     *            The <code>java.sql.Type</code> column type.
     * @return An add column language element to define the column.
     */
    public AddColumn addColumn(String name, int columnType)
    {
        Column column = newColumn(name, 0);
        column.setDefaults(columnType);
        addColumns.add(column);
        return new AddColumn(this, column);
    }
    
    /**
     * Add a new column to the table with the given name and a column type
     * appropriate for the given Java primitive.
     * 
     * @param name
     *            The column name.
     * @param nativeType
     *            The native column type.
     * @return An add column language element to define the column.
     */
    public AddColumn addColumn(String name, Class<?> nativeType)
    {
        Column column = newColumn(name, 0);
        column.setDefaults(nativeType);
        addColumns.add(column);
        return new AddColumn(this, column);
    }

    /**
     * Begin an alter column statement to alter the column with the given name.
     * 
     * @param name
     *            The name of the column to alter.
     * @return An alter column language element to define the column changes.
     */
    public AlterColumn alterColumn(String name)
    {
        Column column = newColumn(name, 0);
        updates.add(new ColumnAlteration(tableName, name, column));
        return new AlterColumn(this, column);
    }
    
    
    public AlterTable dropColumn(String name) {
        updates.add(new ColumnDrop(tableName, name));
        return this;
    }


    /**
     * Test to see if the given column is not null and that the column name
     * matches the old name before setting it to the new name. Returns true if
     * the column name was set.
     * 
     * @param column
     *            The column.
     * @param newName
     *            The new name.
     * @param oldName
     *            The old name.
     * @return True if the column name was set.
     */
    private boolean renameColumnFrom(Column column, String newName, String oldName)
    {
        if (column != null)
        {
            if (column.getName().equals(newName))
            {
                column.setName(oldName);
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Rename the column from the given old name to the given new name. This
     * method will update all references to the new name in previous addenda to
     * the old name.
     * <p>
     * This method is used when definitions are driven by a library that uses
     * reflection to introspect Java objects to create table definitions. If the
     * Java object property is renamed, and the library is using the Java
     * property name to generate a column name, then any place where the
     * property name is used to define a column name for the table that maps to
     * the Java class is updated with the old property name.
     * <p>
     * It is difficult to illustrate with columns, since the use case involves
     * the Java reflection API. See {@link #renameFrom} for an example of
     * "renaming from" with tables.
     * 
     * @param newName
     *            The new column name.
     * @param oldName
     *            The old column name.
     */
    public AlterColumn renameColumnFrom(String newName, String oldName)
    {
        Column alter = newColumn(newName, 0);
        for (Map<String, Table> addendum : tables.subList(1, tables.size()))
        {
            Table table = addendum.get(tableName);
            if (table != null)
            {
                if (renameColumnFrom(table.getColumns().get(newName), newName, oldName))
                {
                    break;
                }
                for (Map<String, Column> pair : table.getVerifications())
                {
                    for (Map.Entry<String, Column> entry : pair.entrySet())
                    {
                        if (entry.getKey().equals(newName) && renameColumnFrom(entry.getValue(), newName, oldName))
                        {
                            break;
                        }
                    }
                }
            }
        }
        updates.add(new ColumnAlteration(tableName, oldName, alter));
        return new AlterColumn(this, alter);
    }

    /**
     * Terminate the alter column statement and return the addendum parent
     * language element to continue the definition of the addendum.
     * 
     * @return The addendum parent language element.
     */
    public Alter end()
    {
        updates.add(new TableAlteration(tableName, addColumns));
        return addendum;
    }
}
