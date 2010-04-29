package com.goodworkalan.addendum;

public abstract class Migration {
    private final Addenda addenda;
    
    public Migration(Addenda addenda) {
        this.addenda = addenda;
    }
    
    public abstract void create();
    
    public Addendum addendum(Connector connector) {
        return addenda.addendum(connector);
    }
    
    public Addendum addendum(Connector connector, Dialect dialect) {
        return addenda.addendum(connector, dialect);
    }

    public Addendum addendum() {
        return addenda.addendum();
    }
}
