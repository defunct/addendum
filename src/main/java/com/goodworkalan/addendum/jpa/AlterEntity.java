package com.goodworkalan.addendum.jpa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.goodworkalan.addendum.ExtensionElement;
import com.goodworkalan.addendum.Schema;
import com.goodworkalan.addendum.AlterTable;
import com.goodworkalan.addendum.VerifyColumn;

public class AlterEntity extends ExtensionElement
{
    private final EntityInfo entityInfo;
    
    private final Queue<Update> updates;
    
    private final Set<String> propertySeen;
    
    public AlterEntity(Class<?> entityClass)
    {
        this.entityInfo = EntityInfo.getInstance(entityClass);
        this.updates = new LinkedList<Update>();
        this.propertySeen = new HashSet<String>();
    }
    
    public AlterEntity addProperty(String name)
    {
        propertySeen.add(name);
        return this;
    }

    public AlterEntity updateProperty(String name)
    {
        propertySeen.add(name);
        return this;
    }
    
    public AlterEntity renamePropertyFrom(String name, String oldName)
    {
        propertySeen.add(name);
        return this;
    }
    
    public AlterEntity renameFrom(String oldName)
    {
        updates.add(new RenameEntity(entityInfo, oldName));
        return this;
    }
    
    @Override
    protected void ending(Schema schema)
    {
        for (Update update : updates)
        {
            update.update(schema);
        }
        AlterTable table = schema.alterTable(entityInfo.getName());
        for (PropertyInfo prop : entityInfo.getProperties().values())
        {
            VerifyColumn column = table.verifyColumn(prop.getName(), prop.getType());
            prop.setColumn(column);
        }
    }
}
