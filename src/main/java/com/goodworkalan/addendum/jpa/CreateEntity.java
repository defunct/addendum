package com.goodworkalan.addendum.jpa;

import java.util.ArrayList;
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
            CreateColumn newColumn = newTable.column(prop.getName(), prop.getType());
            if (prop.isId())
            {
                pk.add(prop.getName());
            }
            prop.define(newColumn);
            newColumn.end();
        }
        newTable.primaryKey(pk.toArray(new String[pk.size()]));
        newTable.end();
    }
}
