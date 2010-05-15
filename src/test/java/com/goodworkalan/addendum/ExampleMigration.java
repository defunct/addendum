package com.goodworkalan.addendum;

public class ExampleMigration  {
    private final Addenda addenda;

    public ExampleMigration(Addenda addenda) {
        this.addenda = addenda;
    }
    
    public Addendum addendum() {
        return addenda.addendum();
    }

    public void create() {
        addendum()
            .create("Employee")
                .add("firstName", String.class).end()
                .add("lastName", String.class).end()
                .add("city", String.class).end()
                .end()
            .create("TaxRate", "tax_rate")
                .add("description", String.class).end()
                .add("rate", float.class).end()
                .end()
            .commit();
        addendum()
            .apply(new BlogDefinition())
            .createIfAbsent()
            .commit();
        addendum()
            .rename("Employee").to("Person")
            .commit();
        addendum()
            .alter("Person")
                .alter("firstName").length(64).end()
                .add("age", int.class).defaultValue(0).end()
                .end()
            .commit();
    }
}
