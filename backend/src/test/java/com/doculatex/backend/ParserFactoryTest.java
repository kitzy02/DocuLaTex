package com.doculatex.backend;

import com.doculatex.backend.parser.DocumentParser;
import com.doculatex.backend.parser.ParserFactory;
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
        // Arrange
        when(pdfParser.supports("pdf")).thenReturn(true);
        when(docxParser.supports("pdf")).thenReturn(false);

        // Act
        DocumentParser result = factory.getParser("pdf");

        // Assert
        assertEquals(pdfParser, result);
        verify(pdfParser).supports("pdf");
    }

    @Test
    void shouldThrowExceptionWhenUnsupported() {
        // Arrange
        when(pdfParser.supports("txt")).thenReturn(false);
        when(docxParser.supports("txt")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.getParser("txt"));
        
        assertEquals("Unsupported type: txt", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionForBlankInput(String invalidInput) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.getParser(invalidInput));
        
        assertEquals("File type is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullInput() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> factory.getParser(null));
        
        assertEquals("File type is required", exception.getMessage());
    }
}