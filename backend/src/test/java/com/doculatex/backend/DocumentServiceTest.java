package com.doculatex.backend;
import com.doculatex.backend.exception.DocumentParsingException;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.parser.DocumentParser;  // IMPORT ADDED
import com.doculatex.backend.parser.ParserFactory;   // IMPORT ADDED
import com.doculatex.backend.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    private ParserFactory parserFactory;
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        parserFactory = mock(ParserFactory.class);
        documentService = new DocumentService(parserFactory);
    }

    @Test
    void shouldThrowExceptionWhenFileIsNull() {
        assertThrows(DocumentParsingException.class,
                () -> documentService.parseDocument(null));
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        // Corrected constructor to include a filename, bypassing the name check
        MultipartFile emptyFile =
                new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[0]);

        assertThrows(DocumentParsingException.class,
                () -> documentService.parseDocument(emptyFile));
    }

    @Test
    void shouldThrowExceptionWhenFilenameIsInvalid() {
        MultipartFile file =
                new MockMultipartFile("file", "invalid", "text/plain",
                        "data".getBytes());

        assertThrows(DocumentParsingException.class,
                () -> documentService.parseDocument(file));
    }

    @Test
    void shouldThrowExceptionWhenUnsupportedExtension() {
        MultipartFile file =
                new MockMultipartFile("file", "test.xyz",
                        "text/plain", "data".getBytes());

        when(parserFactory.getParser("xyz"))
                .thenThrow(new IllegalArgumentException());

        assertThrows(DocumentParsingException.class,
                () -> documentService.parseDocument(file));
    }

    @Test
    void shouldParseSuccessfully() throws Exception {
        MultipartFile file =
                new MockMultipartFile("file", "test.pdf",
                        "application/pdf", "dummy".getBytes());

        DocumentParser parser = mock(DocumentParser.class);
        DocumentContent content = new DocumentContent();

        when(parserFactory.getParser("pdf")).thenReturn(parser);
        when(parser.parse(any())).thenReturn(content);

        DocumentContent result = documentService.parseDocument(file);

        assertNotNull(result);
        verify(parserFactory, times(1)).getParser("pdf");
        verify(parser, times(1)).parse(any());
    }

    @Test
    void shouldWrapExceptionFromParser() throws Exception {
        MultipartFile file =
                new MockMultipartFile("file", "test.pdf",
                        "application/pdf", "dummy".getBytes());

        DocumentParser parser = mock(DocumentParser.class);

        when(parserFactory.getParser("pdf")).thenReturn(parser);
        when(parser.parse(any()))
                .thenThrow(new RuntimeException("boom"));

        assertThrows(DocumentParsingException.class,
                () -> documentService.parseDocument(file));
    }
}