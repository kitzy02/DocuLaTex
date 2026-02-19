package com.doculatex.backend.parser;

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

        PDPageContentStream contentStream =
                new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Paragraph One");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Paragraph Two");
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();

        InputStream inputStream =
                new ByteArrayInputStream(out.toByteArray());

        DocumentContent result = parser.parse(inputStream);

        assertNotNull(result);
        assertEquals("Parsed PDF", result.getTitle());
        assertEquals(1, result.getSections().size());

        DocumentSection section = result.getSections().get(0);
        assertFalse(section.getContentBlocks().isEmpty());
    }
}
