package com.doculatex.backend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentParserTest {

    @Test
    void docxParserShouldImplementDocumentParser() {
        DocumentParser parser = new DocxDocumentParser();
        assertNotNull(parser);
        assertTrue(parser instanceof DocumentParser);
    }
}
