package com.goodworkalan.addendum.jpa;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

/**
 * Unit tests for the {@link DefinitionGenerator} class.
 *
 * @author Alan Gutierrez
 */
public class DefinitionGeneratorTest {
    /** Test generation. */
    @Test
    public void generation() throws DOMException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException  {
        DefinitionGenerator.generate("com.goodworkalan.addendum.jpa.CreateBlog");
    }
    
    /** Test constructor to complete coverage. */
    @Test
    public void constructor() {
        new DefinitionGenerator();
    }
}
