package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.ENTITY_MISSING;
import static com.goodworkalan.addendum.Addendum.TABLE_MISSING;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link Schema} class.
 * 
 * @author Alan Gutierrez
 */
public class SchemaTest {
    /** Test for missing entity. */
 //   @Test(expectedExceptions = Danger.class)
    public void entityMissing() {
        try {
            Schema schema = new Schema();
            schema.getEntity("a");
        } catch (Danger e) {
            assertEquals(e.code, ENTITY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test for missing table. */
 //  @Test(expectedExceptions = Danger.class)
    public void tableMissing() {
        try {
            Schema schema = new Schema();
            schema.aliases.put("a", "a");
            schema.getEntity("a");
        } catch (Danger e) {
            assertEquals(e.code, TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test table name not found. */
 //  @Test
    public void entityNameNotFound() {
        Schema schema = new Schema();
        schema.aliases.put("a", "a");
        schema.aliases.put("b", "b");
        assertNull(schema.getEntityName("c"));
    }
}
