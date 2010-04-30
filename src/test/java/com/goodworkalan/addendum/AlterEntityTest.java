package com.goodworkalan.addendum;

import static org.testng.Assert.assertEquals;

import java.sql.Types;
import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AlterEntity} class.
 * 
 * @author Alan Gutierrez
 */
public class AlterEntityTest {
    /** Test column type setting. */
    @Test
    public void setColumnType() {
        Patch patch = new Patch(new Schema(), new ArrayList<DatabaseUpdate>());
        patch.schema.aliases.put("a", "a");
        patch.schema.entities.put("a", new Entity("a"));
        AlterEntity alter = new AlterEntity(null,  patch.schema.getEntity("a"), patch);
        alter.add("a", String.class).end();
        assertEquals(patch.schema.getEntity("a").getColumn("a").getColumnType(), Types.VARCHAR);
    }
}
