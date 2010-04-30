package com.goodworkalan.addendum;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link UpdateDatabase} class.
 * 
 * @author Alan Gutierrez
 */
public class NullUpdateDatabaseTest {
    /** Test execute. */
    @Test
    public void execute() {
        new NullUpdateDatabase().execute(null, null);
    }
}
