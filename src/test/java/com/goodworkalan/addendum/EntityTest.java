package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.COLUMN_MISSING;
import static com.goodworkalan.addendum.Addendum.PROPERTY_MISSING;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link Entity} class.
 *
 * @author Alan Gutierrez
 */
public class EntityTest {
    /** Test missing property. */
    @Test(expectedExceptions = Danger.class)
    public void propertyMissing() {
        try {
            Entity entity = new Entity("a");
            entity.getColumn("a");
        } catch (Danger e) {
            assertEquals(e.code, PROPERTY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test missing property. */
    @Test(expectedExceptions = Danger.class)
    public void columnMissing() {
        try {
            Entity entity = new Entity("a");
            entity.properties.put("a", "a");
            entity.getColumn("a");
        } catch (Danger e) {
            assertEquals(e.code, COLUMN_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test missing column name. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingColumnName() {
        Entity entity = new Entity("a");
        entity.properties.put("a", "a");
        entity.getPropertyName("b");
    }
}
