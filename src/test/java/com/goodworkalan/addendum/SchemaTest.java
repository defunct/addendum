package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.TABLE_MISSING;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Schema} class.
 * 
 * @author Alan Gutierrez
 */
public class SchemaTest {
    /** Test for missing entity. */
    @Test(expectedExceptions = AddendumException.class)
    public void entityMissing() {
        try {
            Schema schema = new Schema();
            schema.getEntity("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ENTITY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test for missing table. */
    @Test(expectedExceptions = AddendumException.class)
    public void tableMissing() {
        try {
            Schema schema = new Schema();
            schema.aliases.put("a", "a");
            schema.getEntity("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test table name not found. */
    @Test
    public void entityNameNotFound() {
        Schema schema = new Schema();
        schema.aliases.put("a", "a");
        schema.aliases.put("b", "b");
        assertNull(schema.getEntityName("c"));
    }
}
