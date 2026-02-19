package com.doculatex.backend.parser;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class DocxDocumentParserTest {

    private final DocxDocumentParser parser = new DocxDocumentParser();

    @Test
    void shouldParseValidDocx() throws Exception {

        XWPFDocument document = new XWPFDocument();
        document.createParagraph().createRun().setText("Hello World");
        document.createParagraph().createRun().setText("Second Paragraph");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();

        InputStream inputStream =
                new ByteArrayInputStream(out.toByteArray());

        DocumentContent result = parser.parse(inputStream);

        assertNotNull(result);
        assertEquals("Parsed DOCX Document", result.getTitle());
        assertEquals(1, result.getSections().size());

        DocumentSection section = result.getSections().get(0);
        assertNotNull(section);
        assertEquals(2, section.getContentBlocks().size());
    }

    @Test
    void shouldSkipEmptyParagraphs() throws Exception {

        XWPFDocument document = new XWPFDocument();
        document.createParagraph().createRun().setText("   ");
        document.createParagraph().createRun().setText("Valid Text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();

        InputStream inputStream =
                new ByteArrayInputStream(out.toByteArray());

        DocumentContent result = parser.parse(inputStream);

        DocumentSection section = result.getSections().get(0);

        assertEquals(1, section.getContentBlocks().size());
    }

    @Test
    void shouldSupportDocxType() {
        assertTrue(parser.supports("docx"));
        assertTrue(parser.supports("DOCX"));
    }

    @Test
    void shouldNotSupportOtherTypes() {
        assertFalse(parser.supports("pdf"));
        assertFalse(parser.supports("txt"));
    }

    @Test
    void shouldThrowExceptionForInvalidInputStream() {

        InputStream invalidStream =
                new ByteArrayInputStream("invalid content".getBytes());

        assertThrows(Exception.class,
                () -> parser.parse(invalidStream));
    }
}
