package com.goodworkalan.addendum.jpa;

import static com.goodworkalan.addendum.jpa.AddendumJpaException.PROPERTY_NOT_FOUND;

import com.goodworkalan.addendum.AddColumn;
import com.goodworkalan.addendum.Alter;
import com.goodworkalan.addendum.AlterTable;

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
    
    /** The default value reference. */
    private final DefaultValue defaultValue;

    /**
     * Create an alteration that will create the column mapped to the given
     * property in the given entity.
     * 
     * @param entityInfo
     *            The entity information.
     * @param propertyName
     *            The property name.
     * @param defaultValue
     *            The default value reference.
     */
    public AddProperty(EntityInfo entityInfo, String propertyName, DefaultValue defaultValue)
    {
        this.entityInfo = entityInfo;
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
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
        if (propertyInfo == null)
        {
            throw new AddendumJpaException(PROPERTY_NOT_FOUND).add(entityInfo.getName(), propertyName);
        }
        AlterTable alterTable = alter.alterTable(entityInfo.getTableName());
        AddColumn column = alterTable.addColumn(propertyInfo.getColumnName(), propertyInfo.getType());
        propertyInfo.define(column, defaultValue);
        column.end();
        alterTable.end();
    }
}
