package com.doculatex.backend.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

        // Injecting the mocks into the factory
        factory = new ParserFactory(List.of(pdfParser, docxParser));
    }

    @Test
    void shouldReturnCorrectParserWhenSupported() {
        when(pdfParser.supports("pdf")).thenReturn(true);
        when(docxParser.supports("pdf")).thenReturn(false);

        DocumentParser result = factory.getParser("pdf");

        assertEquals(pdfParser, result);
        verify(pdfParser).supports("pdf");
    }

    @Test
    void shouldHandleCaseSensitivityAndSpaces() {
        // Our factory now trims and lowercases the input
        when(pdfParser.supports("pdf")).thenReturn(true);

        DocumentParser result = factory.getParser("  PDF  ");

        assertEquals(pdfParser, result);
    }

    @Test
    void shouldThrowExceptionWhenUnsupported() {
        when(pdfParser.supports(anyString())).thenReturn(false);
        when(docxParser.supports(anyString())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.getParser("txt"));
        
        assertTrue(exception.getMessage().contains("Unsupported file type"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionForBlankInput(String invalidInput) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.getParser(invalidInput));
        
        assertEquals("File type is required and cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> factory.getParser(null));
    }
}