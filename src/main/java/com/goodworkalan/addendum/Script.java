package com.goodworkalan.addendum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Script {
    public final Database database;
    
    public final Map<String, Table> tables = new HashMap<String, Table>();
    
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
