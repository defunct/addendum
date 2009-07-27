package com.goodworkalan.addendum.jpa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.goodworkalan.addendum.Alter;
import com.goodworkalan.addendum.ExtensionElement;

/**
 * Alter an existing table mapped to an existing JPA entity.
 * 
 * @author Alan Gutierrez
 */
public class AlterEntity extends ExtensionElement<Alter>
{
    /** The entity information. */
    private final EntityInfo entityInfo;
    
    /** A list of alterations to perform. */
    private final Queue<Alteration> alterations;
    
    /** A set of properties whose alterations have been recorded. */
    private final Set<String> propertySeen;
    
    /**
     * Create an extension that will alter the table mapped to the given JPA
     * entity.
     * 
     * @param entityClass
     *            The entity class.
     */
    public AlterEntity(Class<?> entityClass)
    {
        this.entityInfo = EntityInfo.getInstance(entityClass);
        this.alterations = new LinkedList<Alteration>();
        this.propertySeen = new HashSet<String>();
    }

    /**
     * Add the column mapped to the property in the entity class to the table
     * mapped to the entity class.
     * 
     * @param name
     *            The property name.
     * @return A property element to define additional database column
     *         attributes.
     */
    public Property<AlterEntity> addProperty(String name)
    {
        DefaultValue defaultValue = new DefaultValue();
        alterations.add(new AddProperty(entityInfo, name, defaultValue));
        propertySeen.add(name);
        return new Property<AlterEntity>(this, defaultValue);
    }

    /**
     * Update a column mapped to a property in entity class in the table mapped
     * to the entity class.
     * 
     * @param name
     *            The property name.
     * @return A property element to define additional database column
     *         attributes.
     */
    public Property<AlterEntity> alterProperty(String name)
    {
        DefaultValue defaultValue = new DefaultValue();
        alterations.add(new AlterProperty(entityInfo, name, defaultValue));
        propertySeen.add(name);
        return new Property<AlterEntity>(this, defaultValue);
    }

    /**
     * Rename the column with the given old name to the name of the column
     * mapped to a property in entity class in the table mapped to the entity
     * class.
     * 
     * @param name
     *            The property name.
     * @param oldName
     *            The existing column name to rename from.
     * @return A property element to define additional database column
     *         attributes.
     */
    public Property<AlterEntity> renamePropertyFrom(String name, String oldName)
    {
        DefaultValue defaultValue = new DefaultValue();
        alterations.add(new RenameProperty(entityInfo, name, oldName, defaultValue));
        propertySeen.add(name);
        return new Property<AlterEntity>(this, defaultValue);
    }
    
    /**
     * Rename the table with the given old name to the name of the table mapped
     * to the entity class.
     * 
     * @param oldName
     *            The existing table name to rename from.
     * @return This alter entity element to continue the specification of the
     *         alteration.
     */
    public AlterEntity renameFrom(String oldName)
    {
        alterations.add(new RenameEntity(entityInfo, oldName));
        return this;
    }

    /**
     * Add a discriminator column to the table based on the
     * <code>@DiscriminatorColumn</code> annotation of the entity.
     * 
     * @param initialize
     *            The initialization value to use to populate the column.
     * @return This alter entity element to continue the specification of the
     *         alteration.
     */
    public AlterEntity addDescriminator(Object initialize)
    {
        alterations.add(new AddDiscriminator(entityInfo, initialize));
        return this;
    }

    /**
     * Define the table mapped to the entity class property.
     * 
     * @param create
     *            A create element form the addendum domain-specific language.
     */
    @Override
    protected void ending(Alter alter)
    {
        for (Alteration alteration : alterations)
        {
            alteration.alter(alter);
        }
    }
}
