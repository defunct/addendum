package com.goodworkalan.addendum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Script {
    public final Schema schema;
    
    public final Map<String, String> aliases = new HashMap<String, String>();

    public final Map<String, Entity> entities = new HashMap<String, Entity>();
    
    private final List<UpdateDatabase> databaseUpdates;
    
    public Script(Schema database, List<UpdateDatabase> databaseUpdates) {
        this.schema = database;
        this.databaseUpdates = databaseUpdates;
    }
    
    public void add(UpdateSchema update) {
        databaseUpdates.add(update.execute(schema));
    }
}
