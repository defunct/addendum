package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Alter;
import com.goodworkalan.addendum.AlterColumn;

/**
 * An alteration that renames a column from an existing name to the name of the
 * column mapped to a property in entity class in the table mapped to the entity
 * class.
 * 
 * @author Alan Gutierrez
 */
class RenameProperty implements Alteration
{
    /** The entity information. */
    private final EntityInfo entityInfo;
    
    /** The property name. */
    private final String propertyName;
    
    /** The existing column name. */
    private final String columnName;

    /**
     * Create an alteration that renames the column for the given property in
     * the given entity to from the given existing column name to the column
     * name mapped to the property.
     * 
     * @param entityInfo
     *            The entity information.
     * @param propertyName
     *            The property name.
     * @param columnName
     *            The existing column name.
     */
    public RenameProperty(EntityInfo entityInfo, String propertyName, String columnName)
    {
        this.entityInfo = entityInfo;
        this.propertyName = propertyName;
        this.columnName = columnName;
    }
    
    /**
     * Define a property rename using the addendum.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter)
    {
        PropertyInfo propertyInfo = entityInfo.getProperties().get(propertyName);
        AlterColumn alterColumn = 
            alter
                .alterTable(entityInfo.getTableName())
                    .renameColumnFrom(propertyInfo.getColumnName(), columnName);
        propertyInfo.define(alterColumn);
        alterColumn.end();
    }
}
