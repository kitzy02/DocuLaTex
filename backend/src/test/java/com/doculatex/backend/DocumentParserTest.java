package com.doculatex.backend;
import com.doculatex.backend.parser.DocumentParser;
import com.doculatex.backend.parser.DocxDocumentParser;
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
