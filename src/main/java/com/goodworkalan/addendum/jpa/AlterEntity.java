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
     * @return This alter entity element to continue the specification of the
     *         alteration.
     */
    public AlterEntity addProperty(String name)
    {
        alterations.add(new AddProperty(entityInfo, name));
        propertySeen.add(name);
        return this;
    }

    /**
     * Update a column mapped to a property in entity class in the table mapped
     * to the entity class.
     * 
     * @param name
     *            The property name.
     * @return This alter entity element to continue the specification of the
     *         alteration.
     */
    public AlterEntity alterProperty(String name)
    {
        alterations.add(new AlterProperty(entityInfo, name));
        propertySeen.add(name);
        return this;
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
     * @return This alter entity element to continue the specification of the
     *         alteration.
     */
    public AlterEntity renamePropertyFrom(String name, String oldName)
    {
        propertySeen.add(name);
        return this;
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
