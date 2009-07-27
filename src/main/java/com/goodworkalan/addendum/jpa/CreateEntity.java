package com.goodworkalan.addendum.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.addendum.Create;
import com.goodworkalan.addendum.CreateColumn;
import com.goodworkalan.addendum.CreateTable;
import com.goodworkalan.addendum.ExtensionElement;

/**
 * Create the table mapped to a JPA entity.
 * 
 * @author Alan Gutierrez
 */
public class CreateEntity extends ExtensionElement<Create>
{
    /** The entity information. */
    private final EntityInfo entityInfo;
    
    /** A map of property names to default value reference. */
    private final Map<String, DefaultValue> defaultValues;

    /**
     * Create an extension that will define the table mapped to the given JPA
     * entity.
     * 
     * @param entityClass
     *            The entity class.
     */
    public CreateEntity(Class<?> entityClass)
    {
        this.entityInfo = EntityInfo.getInstance(entityClass);
        this.defaultValues = new HashMap<String, DefaultValue>();
    }

    /**
     * Create a property element for the given property to define additional
     * column attributes.
     * 
     * @return A property element to define additional database column
     *         attributes.
     */
    public Property<CreateEntity> property(String name)
    {
        if (defaultValues.containsKey(name))
        {
            throw new AddendumJpaException(0);
        }
        DefaultValue defaultValue = new DefaultValue();
        defaultValues.put(name, defaultValue);
        return new Property<CreateEntity>(this, defaultValue);
    }
    
    /**
     * Define the table mapped to the entity class property.
     * 
     * @param create
     *            A create element form the addendum domain-specific language.
     */
    @Override
    protected void ending(Create create)
    {
        List<String> pk = new ArrayList<String>();
        CreateTable newTable = create.createTable(entityInfo.getName());
        for (Map.Entry<String, PropertyInfo> entry : entityInfo.getProperties().entrySet())
        {
            PropertyInfo prop = entry.getValue();
            CreateColumn newColumn = newTable.column(prop.getColumnName(), prop.getType());
            if (prop.isId())
            {
                pk.add(prop.getName());
            }
            DefaultValue defaultValue = defaultValues.get(prop.getName());
            if (defaultValue == null)
            {
                defaultValue = new DefaultValue();
            }
            prop.define(newColumn, defaultValue);
            newColumn.end();
        }
        newTable.primaryKey(pk.toArray(new String[pk.size()]));
        newTable.end();
    }
}
