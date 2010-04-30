package com.goodworkalan.addendum.jpa;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class DefinitionGeneratorTest {
    @Test
    public void nothing() throws DOMException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException  {
        DefinitionGenerator.generate("com.goodworkalan.addendum.jpa.CreateBlog");
    }
}
