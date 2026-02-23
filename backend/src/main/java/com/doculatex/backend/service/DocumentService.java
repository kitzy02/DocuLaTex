package com.doculatex.backend.service;

import com.doculatex.backend.exception.DocumentParsingException;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.parser.DocumentParser;
import com.doculatex.backend.parser.ParserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ParserFactory parserFactory;

    /**
     * Parses the uploaded multipart file into structured DocumentContent.
     * Ensures the file input stream is properly closed after processing.
     */
    public DocumentContent parseDocument(MultipartFile file) {
        // 1. Basic Validation
        if (file == null || file.isEmpty()) {
            throw new DocumentParsingException("File is empty or missing.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new DocumentParsingException("Invalid file name: extension missing.");
        }

        // 2. Extract Extension
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        // 3. Obtain the stream and parse
        // try-with-resources ensures file.getInputStream() is closed automatically
        try (InputStream inputStream = file.getInputStream()) {
            
            DocumentParser parser = parserFactory.getParser(extension);
            return parser.parse(inputStream);

        } catch (DocumentParsingException ex) {
            // Re-throw our specific business exceptions
            throw ex;
        } catch (Exception ex) {
            // Wrap any unknown IO or parsing errors in our custom exception
            throw new DocumentParsingException("Critical failure during parsing: " + ex.getMessage(), ex);
        }
    }
}