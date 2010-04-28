package com.goodworkalan.addendum;

import java.util.List;

public class Script {
    private final Database database;
    
    private final List<Update> updates;
    
    public Script(Database database, List<Update> updates) {
        this.database = database;
        this.updates = updates;
    }
    
    public void add(Update update) {
        update.execute(database);
        updates.add(update);
    }
}
