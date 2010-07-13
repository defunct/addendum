package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.ADDENDUM_ENTITY_MISSING;
import static com.goodworkalan.addendum.Addendum.ADDENDUM_TABLE_MISSING;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link Patch} class.
 *
 * @author Alan Gutierrez
 */
public class PatchTest {
    /** Test lookup of an entity that is missing. */
    @Test(expectedExceptions = Danger.class)
    public void entityMissing() {
        try {
            Patch patch = new Patch(new Schema(), new ArrayList<DatabaseUpdate>());
            patch.getEntity("a");
        } catch (Danger e) {
            assertEquals(e.code, ADDENDUM_ENTITY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test lookup of an entity table that is missing. */
    @Test(expectedExceptions = Danger.class)
    public void tableMissing() {
        try {
            Patch patch = new Patch(new Schema(), new ArrayList<DatabaseUpdate>());
            patch.aliases.put("a", "a");
            patch.getEntity("a");
        } catch (Danger e) {
            assertEquals(e.code, ADDENDUM_TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
