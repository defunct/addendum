package com.goodworkalan.addendum;

public final class CreateEntity extends DefineEntity {
    /** The database migration script. */
    private final Script script;
    
    /** The entity name. */
    private final String entityName;

    /**
     * Create a create entity language element that will record to the given
     * <code>entity</code> and add a create entity update to the given migration
     * <code>script</code>. The given <code>addendum</code> is returned when the
     * create entity statement terminates.
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
