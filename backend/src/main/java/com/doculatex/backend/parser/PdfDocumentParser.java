package com.doculatex.backend.parser;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import com.doculatex.backend.model.ParagraphBlock;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PdfDocumentParser implements DocumentParser {

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB Limit

    @Override
    public DocumentContent parse(InputStream inputStream) throws Exception {
        // Logic for Phase 1: Read safely
        byte[] allBytes = inputStream.readNBytes((int) MAX_SIZE);
        
        // Changed Loader.loadPDF(allBytes) to PDDocument.load(allBytes) for PDFBox 2.x compatibility
        try (PDDocument pdf = PDDocument.load(allBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);

            DocumentContent doc = new DocumentContent();
            doc.setTitle("Parsed PDF");
            DocumentSection main = new DocumentSection("Main Content", 1);

            // Relaxed split: double newline
            String[] chunks = text.split("\\n\\s*\\n");
            for (String chunk : chunks) {
                if (!chunk.trim().isEmpty()) {
                    main.getContentBlocks().add(new ParagraphBlock(chunk.trim()));
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