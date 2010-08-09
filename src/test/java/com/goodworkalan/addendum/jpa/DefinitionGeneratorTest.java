package com.goodworkalan.addendum.jpa;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link DefinitionGenerator} class.
 *
 * @author Alan Gutierrez
 */
public class DefinitionGeneratorTest {
    /** Test generation. */
    @Test
    public void generation() throws Exception  {
        DefinitionGenerator.generate("com.goodworkalan.addendum.jpa.CreateBlog");
    }
    
    /** Test constructor to complete coverage. */
    @Test
    public void constructor() {
        new DefinitionGenerator();
    }
}
