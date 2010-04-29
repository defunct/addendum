package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Performs a single table rename update against the database.
 *
 * @author Alan Gutierrez
 */
class TableRename implements Update {
    private final String alias;
    /** The existing table name. */
    private final String from;
    
    /** The new table name. */
    private final String to;
    
    /**
     * The existing table name.
     * 
     * @param from
     *            The existing table name.
     * @param to
     *            The new table name.
     */
    public TableRename(String alias, String from, String to) {
        this.alias = alias;
        this.from = from;
        this.to = to;
    }
    
    public void execute(Schema schema) {
        Entity table = schema.tables.remove(from);
        table.setName(to);
        schema.tables.put(to, table);
        schema.aliases.put(alias, to);
    }

    /**
     * Perform a single table rename update against the database.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The SQL dialect.
     * @throws SQLException
     *             For any SQL error.
     * @throws AddendumException
     *             For any error occurring during the update.
     */
    public void execute(Connection connection, Dialect dialect) throws SQLException
    {
        dialect.renameTable(connection, from, to);
    }
}
