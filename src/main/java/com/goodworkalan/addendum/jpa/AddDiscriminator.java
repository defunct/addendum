package com.goodworkalan.addendum.jpa;

import com.goodworkalan.addendum.Alter;
import com.goodworkalan.addendum.Column;

/**
 * An alteration that adds the descriminator column of the given entity to the
 * given entity.
 * 
 * @author Alan Gutierrez
 */
class AddDiscriminator implements Alteration
{
    /**  The entity information. */
    private final EntityInfo entityInfo;
    
    /** The value to use to fill the column. */
    private final Object initialize;

    /**
     * Create an alteration that adds the descriminator column of the given
     * entity to the given entity.
     * 
     * @param entityInfo
     *            The entity information.
     * @param initialize
     *            The value to use to fill the column.
     */
    public AddDiscriminator(EntityInfo entityInfo, Object initialize)
    {
        this.entityInfo = entityInfo;
        this.initialize = initialize;
    }

    /**
     * Add a descriminator column for the entity using the addendum alter
     * element.
     * 
     * @param alter
     *            The alter domain-specific language element.
     */
    public void alter(Alter alter)
    {
        Column column = entityInfo.getDiscriminator();
        alter
            .alterTable(entityInfo.getTableName())
                .addColumn(column.getName(), column.getColumnType())
                    .length(column.getLength())
                    .defaultValue(initialize)
                    .end()
                .end();
    }
}
