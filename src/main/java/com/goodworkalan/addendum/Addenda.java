package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.furnish.Furnish;

/**
 * A collection of {@link Addendum} instances with changes to apply to an
 * application's data structures.
 * 
 * @author Alan Gutierrez
 */
public class Addenda {
    /** The numbers of addendums to skip. */
    private final int skip;

    /** The tracking schema. */
    private final Schema schema = new Schema();

    /** A list of changes to apply to the database. */
    private final List<List<DatabaseUpdate>> scripts = new ArrayList<List<DatabaseUpdate>>();

    /** The connector. */
    private final Connector connector;

    /** A service loader for the Dialect service. */
    private Iterable<Dialect> dialects = new Furnish<Dialect>(Dialect.class);

    /**
     * Create a collection of changes that operates on the database associated
     * with the given connector.
     * 
     * @param connector
     *            The database connector.
     * @param skip
     *            The number of initial addenda to skip.
     */
    public Addenda(Connector connector, int skip) {
        this.connector = connector;
        this.skip = skip;
    }

    /**
     * Create a collection of changes that operates on the database associated
     * with the given connector.
     * 
     * @param connector
     *            The database connector.
     */
    public Addenda(Connector connector) {
        this(connector, 0);
    }

    /**
     * Apply all of the addenda if they are not already recored in the addenda
     * table in the database of the associated connector. A dialect will be
     * chosen from the dialects available according to their
     * <code>META-INF/com.goodworkalan.addendum.Dialect</code> files.
     * 
     * @exception AddendumException
     *                For any SQL error.
     */
    public void amend() {
        Connection connection = connector.open();
        try {
            Dialect dialect = null;
            try {
                for (Dialect candidate : dialects) {
                    dialect = candidate.canTranslate(connection, dialect);
                }
            } catch (SQLException e) {
                throw new AddendumException(SQL_GET_DIALECT, e);
            }
            if (dialect == null) {
                throw new AddendumException(SQL_GET_DIALECT);
            }
            try {
                dialect.createAddendaTable(connection);
            } catch (SQLException e) {
                throw new AddendumException(SQL_CREATE_ADDENDA, e);
            }
            int max;
            try {
                max = dialect.addendaCount(connection);
            } catch (SQLException e) {
                throw new AddendumException(SQL_ADDENDA_COUNT, e);
            }
            for (int i = max, stop = scripts.size(); i < stop; i++) {
                if (i >= skip) {
                    for (DatabaseUpdate update : scripts.get(i)) {
                        update.update(connection, dialect);
                    }
                }
                try {
                    dialect.addendum(connection);
                } catch (SQLException e) {
                    throw new AddendumException(SQL_ADDENDUM, e);
                }
            }
        } finally {
            connector.close(connection);
        }
    }
    
    /**
     * Create a new addendum that will changes to a the database associated with
     * the connector of this addenda, or to any other data resources in the
     * application.
     * 
     * @return An addendum builder used to specify updates to the database.
     */
    public Addendum addendum() {
        List<DatabaseUpdate> updates = new ArrayList<DatabaseUpdate>();
        scripts.add(updates);
        return new Addendum(new Patch(schema, updates));
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */