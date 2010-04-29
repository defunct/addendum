package com.goodworkalan.addendum;

/**
 * Builds a new entity for creation specifying the entity primary key and entity
 * properties.
 * <p>
 * This class is used in the context of a domain-specific language that is
 * implemented as chained Java method calls. Refer to the package documentation
 * for documentation of the language and examples of use.
 * 
 * @author Alan Gutierrez
 */
public final class CreateEntity extends DefineEntity {
    /** The database migration script. */
    private final Script script;
    
    /** The entity name. */
    private final String entityName;

    /**
     * Create a create entity builder that will record to the given entity
     * definition and add a create entity update to the given migration script.
     * The given addendum is returned when the create entity builder terminates.
     * 
     * @param addendum
     *            The addendum to return when the create entity statement
     *            terminates.
     * @param entity
     *            The entity to build.
     * @param script
     *            The migration script.
     */
    public CreateEntity(Addendum addendum, Entity entity, String entityName, Script script) {
        super(addendum, entity);
        this.entityName = entityName;
        this.script = script;
    }
    
    /**
     * Override ending to add a create entity update to the migration script.
     */
    @Override
    protected void ending() {
        script.add(new TableCreate(entityName, entity));
    }
}
