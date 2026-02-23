package com.doculatex.backend.parser;

import com.doculatex.backend.exception.DocumentParsingException;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import com.doculatex.backend.model.ParagraphBlock;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PdfDocumentParser implements DocumentParser {

    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5MB Limit

    @Override
    public DocumentContent parse(InputStream inputStream) throws Exception {
        // 1. Read MAX_SIZE + 1 to detect if the file exceeds our limit
        byte[] allBytes = inputStream.readNBytes((int) MAX_SIZE + 1);
        
        if (allBytes.length > MAX_SIZE) {
            throw new DocumentParsingException("PDF exceeds the allowed 5MB limit");
        }
        
        // 2. Parse the bytes safely using try-with-resources
        try (PDDocument pdf = PDDocument.load(allBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);

            DocumentContent doc = new DocumentContent();
            doc.setTitle("Parsed PDF");
            DocumentSection main = new DocumentSection("Main Content", 1);

            // 3. Logic for Phase 1: Split into paragraphs by double newlines
            if (text != null) {
                String[] chunks = text.split("\\n\\s*\\n");
                for (String chunk : chunks) {
                    if (!chunk.trim().isEmpty()) {
                        main.getContentBlocks().add(new ParagraphBlock(chunk.trim()));
                    }
                }
            }
            
            doc.getSections().add(main);
            return doc;
        }
    }

    @Override
    public boolean supports(String fileType) {
        return "pdf".equalsIgnoreCase(fileType);
    }
}