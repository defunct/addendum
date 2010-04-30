package com.goodworkalan.addendum.jpa;

import org.testng.annotations.Test;

public class DefinitionDocumentTest {
    @Test
    public void noNeedTypes() {
        DefinitionDocument definitionDocument = new DefinitionDocument();
        definitionDocument.className = "com.goodworkalan.cms.BlogMigration";
        definitionDocument.print();
    }
}
