package com.goodworkalan.addendum;

public class AddColumn extends FreshProperty<AlterTable, AddColumn> {
    private final Script script;
    private final String tableName;
    private final String property;

    public AddColumn(AlterTable container, Script script, String tableName, String property, Column column) {
        super(container, column);
        this.script = script;
        this.property = property;
        this.tableName = tableName;
    }
    
    @Override
    protected AddColumn getElement() {
        return this;
    }
    
    @Override
    protected void ending() {
        script.add(new TableAlteration(tableName, property, column));
    }
}
