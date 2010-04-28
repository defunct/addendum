package com.goodworkalan.addendum;


public class RenameTable {
    private final Alteration alteration;

    private final String from;
    
    private final Script script;
    
    public RenameTable(Alteration alteration, Script script, String from) {
        this.alteration = alteration;
        this.from = from;
        this.script = script;
    }

    public Alteration to(String to) {
        script.add(new TableRename(from, to));
        return alteration;
    }
}
