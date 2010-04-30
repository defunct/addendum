package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_MISSING;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Patch} class.
 *
 * @author Alan Gutierrez
 */
public class PatchTest {
    /** Test lookup of an entity that is missing. */
    @Test(expectedExceptions = AddendumException.class)
    public void entityMissing() {
        try {
            Patch patch = new Patch(new Schema(), new ArrayList<DatabaseUpdate>());
            patch.getEntity("a");
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
            Patch patch = new Patch(new Schema(), new ArrayList<DatabaseUpdate>());
            patch.aliases.put("a", "a");
            patch.getEntity("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ADDENDUM_TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
