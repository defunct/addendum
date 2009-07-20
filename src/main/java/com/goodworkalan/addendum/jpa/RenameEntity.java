package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Alter;

/**
 * An alteration that renames a table mapped to an entity from a previous table
 * name to the current table name.
 * 
 * @author Alan Gutierrez
 */
class RenameEntity implements Alteration
{
    /** The current table name. */
    private final String oldName;
    
    /** The entity information. */
    private final EntityInfo entityInfo;

    /**
     * Create a rename entity alteration that will rename the table with the
     * given old name to the name of the table mapped to the given entity
     * information.
     * 
     * @param entityInfo
     *            The entity information.
     * @param oldName
     *            The table to rename from.
     */
    public RenameEntity(EntityInfo entityInfo, String oldName)
    {
        this.entityInfo = entityInfo;
        this.oldName = oldName;
    }

    /**
     * Define a table rename using the addendum.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter)
    {
        alter
            .alterTable(entityInfo.getName())
                .renameFrom(oldName)
                .end();
    }
}
