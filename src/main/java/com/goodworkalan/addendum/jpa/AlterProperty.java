package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Alter;
import com.goodworkalan.addendum.AlterColumn;

/**
 * An alteration that updates a column mapped to a property to capture
 * changes to that columns definition.
 * 
 * @author Alan Gutierrez
 */
class AlterProperty implements Alteration
{
    /** The entity information. */
    private final EntityInfo entityInfo;
    
    /** The property name. */
    private final String propertyName;

    /**
     * Create an alteration that will update the column mapped to the given
     * property in the given entity.
     * 
     * @param entityInfo
     *            The entity information.
     * @param propertyName
     *            The property name.
     */
    public AlterProperty(EntityInfo entityInfo, String propertyName)
    {
        this.entityInfo = entityInfo;
        this.propertyName = propertyName;
    }
    
    
    /**
     * Define a column alteration using the addendum.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter)
    {
        PropertyInfo propertyInfo = entityInfo.getProperties().get(propertyName);
        AlterColumn column = alter
            .alterTable(entityInfo.getTableName())
                .alterColumn(propertyInfo.getColumnName());
        propertyInfo.define(column);
        column.end();
    }
}
