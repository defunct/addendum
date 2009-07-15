package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Schema;
import com.goodworkalan.addendum.Table;

public class RenameEntity implements Update
{
    private final String oldName;
    
    private final EntityInfo entityInfo;
    
    public RenameEntity(EntityInfo entityInfo, String oldName)
    {
        this.entityInfo = entityInfo;
        this.oldName = oldName;
    }
    
    public void update(Schema schema)
    {
        Table table = schema.alterTable(oldName);
        table.rename(entityInfo.getName());
        table.end();
    }
}
