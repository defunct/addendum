package com.goodworkalan.addendum;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link DatabaseUpdate} class.
 * 
 * @author Alan Gutierrez
 */
public class NullDatabaseUpdateTest {
    /** Test execute. */
    @Test
    public void execute() {
        new NullDatabaseUpdate().execute(null, null);
    }
}
