package com.goodworkalan.addendum;

public class Alteration {
    private final Addendum addendum;

    private final Script script;
    
    public Alteration(Addendum addendum, Script script) {
        this.addendum = addendum;
        this.script = script;
    }
    
    public RenameTable rename(String from) {
        return new RenameTable(this, script, from);
    }
    
    public AlterTable table(String name) {
        Table table = script.database.tables.get(name);
        if (table == null) {
            throw new AddendumException(0, name);
        }
        return new AlterTable(this, table, script);
    }
    
    public Addendum end() {
        return addendum;
    }
}
