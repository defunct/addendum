package com.goodworkalan.addendum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Script {
    public final Schema database;
    
    public final Map<String, Entity> tables = new HashMap<String, Entity>();
    
    private final List<UpdateDatabase> updates;
    
    public Script(Schema database, List<UpdateDatabase> updates) {
        this.database = database;
        this.updates = updates;
    }
    
    public void add(UpdateSchema update) {
        updates.add(update.execute(database));
    }
}
