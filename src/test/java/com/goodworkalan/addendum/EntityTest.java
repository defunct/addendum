package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Entity} class.
 *
 * @author Alan Gutierrez
 */
public class EntityTest {
    /** Test missing property. */
    @Test(expectedExceptions = AddendumException.class)
    public void propertyMissing() {
        try {
            Entity entity = new Entity("a");
            entity.getColumn("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PROPERTY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test missing property. */
    @Test(expectedExceptions = AddendumException.class)
    public void columnMissing() {
        try {
            Entity entity = new Entity("a");
            entity.properties.put("a", "a");
            entity.getColumn("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), COLUMN_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
