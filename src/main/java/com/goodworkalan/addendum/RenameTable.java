package com.goodworkalan.addendum;


public class RenameTable {
    private final Addendum addendum;

    private final String from;

    private final Script script;

    public RenameTable(Addendum addendum, Script script, String from) {
        this.addendum = addendum;
        this.from = from;
        this.script = script;
    }

    public Addendum to(String to) {
        script.add(new AliasRename(from, to));
        Database schema = script.database;
        Table table = schema.tables.get(schema.aliases.get(to));
        if (table.getName().equals(from)) {
            script.add(new TableRename(to, from, to));
        }
        return addendum;
    }
}
