package com.doculatex.backend.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParserFactoryTest {

    private ParserFactory factory;
    private DocumentParser pdfParser;
    private DocumentParser docxParser;

    @BeforeEach
    void setup() {
        pdfParser = mock(DocumentParser.class);
        docxParser = mock(DocumentParser.class);

        factory = new ParserFactory(List.of(pdfParser, docxParser));
    }

    @Test
    void shouldReturnCorrectParserWhenSupported() {
        when(pdfParser.supports("pdf")).thenReturn(true);
        when(docxParser.supports("pdf")).thenReturn(false);

        DocumentParser result = factory.getParser("pdf");

        assertEquals(pdfParser, result);
    }

    @Test
    void shouldReturnDocxParserWhenSupported() {
        when(pdfParser.supports("docx")).thenReturn(false);
        when(docxParser.supports("docx")).thenReturn(true);

        DocumentParser result = factory.getParser("docx");

        assertEquals(docxParser, result);
    }

    @Test
    void shouldThrowExceptionWhenUnsupported() {
        when(pdfParser.supports(anyString())).thenReturn(false);
        when(docxParser.supports(anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> factory.getParser("txt"));
    }
}
