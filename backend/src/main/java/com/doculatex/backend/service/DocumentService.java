package com.doculatex.backend.service;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.parser.ParserFactory;
import com.doculatex.backend.parser.DocumentParser;
import com.doculatex.backend.exception.DocumentParsingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    private final ParserFactory parserFactory;

    public DocumentService(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public DocumentContent parseDocument(MultipartFile file) {
        try {
            // 1. Basic Validation
            if (file == null || file.isEmpty()) {
                throw new DocumentParsingException("File is empty or missing.");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.contains(".")) {
                throw new DocumentParsingException("Invalid file name: extension missing.");
            }

            // 2. Extract Extension
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

            // 3. Delegate to Factory and Parser
            DocumentParser parser = parserFactory.getParser(extension);
            return parser.parse(file.getInputStream());

        } catch (DocumentParsingException ex) {
            // Re-throw our specific error
            throw ex; 
        } catch (Exception ex) {
            // Wrap any unknown errors (like IO issues) in our custom exception
            throw new DocumentParsingException("Critical failure during parsing: " + ex.getMessage(), ex);
        }
    }
}