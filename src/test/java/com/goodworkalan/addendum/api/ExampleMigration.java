package com.goodworkalan.addendum.api;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.Migration;

public class ExampleMigration extends Migration {
    public ExampleMigration(Addenda addenda) {
        super(addenda);
    }

    @Override
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
            .apply(BlogDefinition.class)
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
