package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.List;

/**
 * Begins an alter table statement in the domain-specific language for defining
 * database migrations.
 * 
 * @author Alan Gutierrez
 */
public class AlterTable {
    /**
     * The domain-specific language element that defines a single database
     * migration.
     */
    private final Addendum addendum;

    private final Script script;
    
    private final Entity table;
    
    /** The list of column definitions of columns to add to the table. */
    private final List<Column> addColumns;

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
    AlterTable(Addendum addendum, Entity table, Script script) {
        this.addendum = addendum;
        this.table = table;
        this.script = script;
        this.addColumns = new ArrayList<Column>();
    }

    public RenameColumn rename(String from) {
        Column column = table.getColumns().get(from);
        if (column == null) {
            throw new AddendumException(0, from);
        }
        return new RenameColumn(this, script, table.getName(), new Column(column), from);
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
    public AddColumn addColumn(String name, int columnType) {
        if (table.getColumns().containsKey("name")) {
            throw new AddendumException(0, name);
        }
        Column column = new Column(name, columnType);
        column.setDefaults(columnType);
        addColumns.add(column);
        return new AddColumn(this, script, table.getName(), column);
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
    public AddColumn add(String name, Class<?> nativeType) {
        if (table.getColumns().containsKey(name)) {
            throw new AddendumException(0, name);
        }
        Column column = new Column(name, nativeType);
        column.setDefaults(nativeType);
        addColumns.add(column);
        return new AddColumn(this, script, table.getName(), column);
    }

    /**
     * Begin an alter column statement to alter the column with the given name.
     * 
     * @param name
     *            The name of the column to alter.
     * @return An alter column language element to define the column changes.
     */
    public AlterColumn alter(String name) {
        Column column = table.getColumns().get(name);
        if (column == null) {
            throw new AddendumException(0, name);
        }
        return new AlterColumn(this, script, table.getName(), new Column(column));
    }
    
    
    public AlterTable drop(String name) {
        script.add(new ColumnDrop(table.getName(), name));
        return this;
    }

    public Addendum end () {
        return addendum;
    }
}
