package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public class ColumnMove implements Update {
    private final String entityName;

    private final String from;
    
    private final String to;
    
    public ColumnMove(String entityName, String from, String to) {
        this.entityName = entityName;
        this.from = from;
        this.to = to;
    }
    
    public UpdateDatabase execute(Schema schema) {
        Entity entity = schema.getEntity(entityName);
        if (!entity.properties.containsKey(from)) {
            throw new AddendumException(0, entityName, from, to);
        }
        if (entity.properties.containsKey(to)) {
            throw new AddendumException(0, entityName, from, to);
        }
        entity.properties.put(to, entity.properties.remove(from));
        return new UpdateDatabase() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
            }
        };
    }
}
