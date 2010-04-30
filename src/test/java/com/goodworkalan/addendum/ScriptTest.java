package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_MISSING;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Script} class.
 *
 * @author Alan Gutierrez
 */
public class ScriptTest {
    /** Test lookup of an entity that is missing. */
    @Test(expectedExceptions = AddendumException.class)
    public void entityMissing() {
        try {
            Script script = new Script(new Schema(), new ArrayList<UpdateDatabase>());
            script.getEntity("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ADDENDUM_ENTITY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test lookup of an entity table that is missing. */
    @Test(expectedExceptions = AddendumException.class)
    public void tableMissing() {
        try {
            Script script = new Script(new Schema(), new ArrayList<UpdateDatabase>());
            script.aliases.put("a", "a");
            script.getEntity("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ADDENDUM_TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
