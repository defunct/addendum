package com.goodworkalan.addendum;

public class RenameColumn {
    private final AlterTable alterTable;

    private final String from;
    private final String tableName;
    private final Script script;

    private final Column column;

    public RenameColumn(AlterTable alterTable, Script script, String tableName, Column column, String from) {
        this.alterTable = alterTable;
        this.from = from;
        this.column = column;
        this.script = script;
        this.tableName = tableName;
    }

    public AlterTable to(String to) {
        column.setName(to);
        script.add(new ColumnAlteration(tableName, from, column));
        return alterTable;
    }
}
