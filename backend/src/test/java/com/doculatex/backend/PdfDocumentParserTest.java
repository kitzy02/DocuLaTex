package com.doculatex.backend.parser;

import com.doculatex.backend.exception.DocumentParsingException;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PdfDocumentParserTest {

    private final PdfDocumentParser parser = new PdfDocumentParser();

    @Test
    void shouldParseValidPdf() throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Paragraph One");
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());

        DocumentContent result = parser.parse(inputStream);

        assertNotNull(result);
        assertEquals("Parsed PDF", result.getTitle());
        assertEquals(1, result.getSections().size());
        assertFalse(result.getSections().get(0).getContentBlocks().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenPdfExceedsMaxSize() {
        // Create a fake stream that is exactly 5MB + 1 byte
        byte[] largeData = new byte[(5 * 1024 * 1024) + 1];
        InputStream inputStream = new ByteArrayInputStream(largeData);

        // Verify that the parser throws our custom exception
        DocumentParsingException exception = assertThrows(
            DocumentParsingException.class, 
            () -> parser.parse(inputStream)
        );

        assertTrue(exception.getMessage().contains("exceeds the allowed 5MB limit"));
    }
}