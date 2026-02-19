package com.doculatex.backend.exception;

/**
 * Custom exception for errors occurring during the document 
 * parsing or LaTeX generation process.
 */
public class DocumentParsingException extends RuntimeException {

    // Constructor for just a message
    public DocumentParsingException(String message) {
        super(message);
    }

    // Constructor for a message and the original cause (like an IOException)
    public DocumentParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}