package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.AddColumn;
import com.goodworkalan.addendum.Alter;

/**
 * An alteration that adds a column mapped to a property for an entity whose
 * mapping table already exists in the database.
 * 
 * @author Alan Gutierrez
 */
class AddProperty implements Alteration
{
    /** The entity information. */
    private final EntityInfo entityInfo;
    
    /** The property name. */
    private final String propertyName;

    /**
     * Create an alteration that will create the column mapped to the given
     * property in the given entity.
     * 
     * @param entityInfo
     *            The entity information.
     * @param propertyName
     *            The property name.
     */
    public AddProperty(EntityInfo entityInfo, String propertyName)
    {
        this.entityInfo = entityInfo;
        this.propertyName = propertyName;
    }
    
    
    /**
     * Define a column addition using the addendum.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter)
    {
        PropertyInfo propertyInfo = entityInfo.getProperties().get(propertyName);
        AddColumn column = alter
            .alterTable(entityInfo.getTableName())
                .addColumn(propertyInfo.getColumnName(), propertyInfo.getType());
        propertyInfo.define(column);
        column.end();
    }
}
