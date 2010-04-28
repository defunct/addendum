package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The root object of the domain-specific language used to define a database
 * migration.
 * 
 * @author Alan Gutierrez
 */
public class Addendum implements Execute
{
    /** A list of updates to perform. */
    private final List<Update> updates;
    
    /**
     * A list of maps of table definitions by table name, one map for each
     * addendum.
     */
    private final LinkedList<Map<String, Table>> tables;
    
    /** The type of actions being defined on the addendum. */
    private AddendumState state;

    /**
     * Create a new schema.
     * 
     * @param updates
     *            A list to record the updates to perform.
     */
    Addendum(List<Update> updates, LinkedList<Map<String, Table>> tables)
    {
        this.updates = updates;
        this.tables = tables;
        this.state = AddendumState.WAITING;
    }

    /**
     * Begin an execute extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Execute>> T execute(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * Performs updates using application specific SQL statements.
     * 
     * @param executable
     *            An {@link Executable} to execute.
     * @return This schema element to continue the domain-specific language
     *         statement.
     */
    public Execute execute(Executable executable)
    {
        Execution execution = new Execution(executable);
        updates.add(execution);
        return this;
    }
    
    /**
     * Begin a creation extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Create>> T create(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * Create a new table with the given name.
     * 
     * @param name
     *            The table name.
     * @return A create table element to define the new table.
     */
    public CreateTable table(String name)
    {
        if (state.ordinal() > AddendumState.CREATING.ordinal())
        {
            throw new AddendumException(0);
        }
        state = AddendumState.CREATING;
        if (tables.getFirst().containsKey(name))
        {
            throw new IllegalStateException();
        }
        Table table = new Table(name);
        tables.getFirst().put(name, table);
        List<String> primaryKey = new ArrayList<String>();
        updates.add(new TableCreate(table, primaryKey));
        return new CreateTable(this, table, primaryKey);
    }
    
    /**
     * Begin an alter extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Alter>> T alter(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * Alter an existing table with the given name.
     * 
     * @param name
     *            The table name.
     * @return An alter table element to specify changes to the table.
     */
    public AlterTable alterTable(String name)
    {
        if (state.ordinal() > AddendumState.ALTERING.ordinal())
        {
            throw new AddendumException(0);
        }
        state = AddendumState.ALTERING;
        if (!tables.getFirst().containsKey(name))
        {
            tables.getFirst().put(name, new Table(name));
        }
        // FIXME Do not allow rename during first addendum.
        return new AlterTable(this, name, updates, tables);
    }

    /**
     * Begin an assert extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Verify>> T verify(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * Assert that an existing table matches a specified table definition.
     * 
     * @param name
     *            The table name.
     * @return An assert table element to specify the table definition.
     */
    public VerifyTable verifyTable(String name)
    {
        if (state.ordinal() > AddendumState.ASSERTING.ordinal())
        {
            throw new AddendumException(0);
        }
        state = AddendumState.ASSERTING;
        return new VerifyTable(this, updates, tables.getFirst(), name);
    } 
    
    /**
     * Begin an insert extension element.
     * 
     * @param <T>
     *            The extension element type.
     * @param extension
     *            An instance of the extension element.
     * @return The extension element.
     */
    public <T extends ExtensionElement<Populate>> T insert(T extension)
    {
        extension.setAddendum(this);
        return extension;
    }

    /**
     * Create an insert statement that will insert values into the database.
     * 
     * @param table
     *            The name of the table to update.
     * @return An insert element to define the insert statement.
     */
    public Insert insert(String table)
    {
        if (state.ordinal() > AddendumState.POPULATING.ordinal())
        {
            throw new AddendumException(0);
        }
        state = AddendumState.POPULATING;
        Insertion insertion = new Insertion(table);
        updates.add(insertion);
        return new Insert(this, insertion);
    }

    /**
     * Terminates the addendum specification statement in the domain specific
     * language.
     */
    public void commit()
    {
        state = AddendumState.COMMITTED;
    }
}
