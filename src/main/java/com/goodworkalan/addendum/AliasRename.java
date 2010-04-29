package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public class AliasRename implements Update {
    /** The existing alias name. */
    private final String from;
    
    /** The new alias name. */
    private final String to;
    
    public AliasRename(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public void execute(Schema schema) {
        if (schema.aliases.containsKey(to)) {
            throw new AddendumException(0, to);
        }
        schema.aliases.put(to, schema.aliases.remove(from));
    }

    public void execute(Connection connection, Dialect dialect)
    throws SQLException {
    }
}
