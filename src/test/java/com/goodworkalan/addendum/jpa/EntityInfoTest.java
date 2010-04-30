package com.goodworkalan.addendum.jpa;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link EntityInfo} class.
 * 
 * @author Alan Gutierrez
 */
public class EntityInfoTest {
    /** Test failed intropsection. */
    @Test(expectedExceptions = RuntimeException.class)
    public void introspect() {
        EntityInfo.introspect(String.class, Number.class);
    }
}
