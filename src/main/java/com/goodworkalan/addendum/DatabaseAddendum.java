package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * An addendum that uses a domain-specific language to define database updates
 * that are cross-platform. 
 *
 * @author Alan Gutierrez
 */
@ReadBy(NothingToReadReader.class)
public abstract class DatabaseAddendum implements Addendum
{
    /**
     * Create a database addendum.
     */
    public DatabaseAddendum()
    {
    }

    /**
     * Overridden to define an update to the database. The given schema is a
     * domain-specific language used to define database updates that are
     * cross-platform.
     * 
     * @param schema
     *            The root of the domain specific language.
     */
    public abstract void forward(Schema schema);

    /**
     * Perform an update on the database defined by the {@link #forward(Schema)
     * forward} method using the given configuration.
     * 
     * @param connection
     *            A JDBC connection.
     * @throws SQLException
     *             For any SQL error.
     * @throws AddendumException
     *             For any error occuring during the update.
     */
    public void execute(Connection connection) throws SQLException, AddendumException
    {
        List<Update> updates = new ArrayList<Update>();
        Schema schema = new Schema(updates);
        forward(schema);
        Dialect dialect = DialectLibrary.getInstance().getDialect(connection);
        for (Update update : updates)
        {
            update.execute(connection, dialect);
        }
    }
}
