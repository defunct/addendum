package com.goodworkalan.addendum.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.addendum.ExtensionElement;
import com.goodworkalan.addendum.NewColumn;
import com.goodworkalan.addendum.NewTable;
import com.goodworkalan.addendum.Schema;

public class CreateEntity extends ExtensionElement
{
    private final EntityInfo entityInfo;
    
    public CreateEntity(Class<?> entityClass)
    {
        this.entityInfo = EntityInfo.getInstance(entityClass);
    }
    
    @Override
    protected void ending(Schema schema)
    {
        List<String> pk = new ArrayList<String>();
        NewTable newTable = schema.createTable(entityInfo.getName());
        for (Map.Entry<String, PropertyInfo> entry : entityInfo.getProperties().entrySet())
        {
            PropertyInfo prop = entry.getValue();
            NewColumn newColumn = newTable.column(prop.getName(), prop.getType());
            if (prop.isId())
            {
                pk.add(prop.getName());
            }
            prop.setColumn(newColumn);
            newColumn.end();
        }
        newTable.primaryKey(pk.toArray(new String[pk.size()]));
        newTable.end();
    }
}
