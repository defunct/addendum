package com.goodworkalan.addendum;

/**
 * An example of a migration definition.
 *
 * @author Alan Gutierrez
 */
public class ExampleMigration  {
    /** The addenda. */
    private final Addenda addenda;

    /**
     * Create an example migration.
     * 
     * @param addenda
     *            The addenda.
     */
    public ExampleMigration(Addenda addenda) {
        this.addenda = addenda;
    }
    
    /**
     * Create a new addendum and add it to the addenda.
     * 
     * @return A new addendum.
     */
    public Addendum addendum() {
        return addenda.addendum();
    }

    /** Create the migration definition. */
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
            .rename("Employee", "Person")
            .commit();
        addendum()
            .alter("Person")
                .alter("firstName").length(64).end()
                .add("age", int.class).defaultValue(0).end()
                .end()
            .commit();
    }
}
