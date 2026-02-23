package com.doculatex.backend.parser;

import com.doculatex.backend.exception.DocumentParsingException;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import com.doculatex.backend.model.ParagraphBlock;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class DocxDocumentParser implements DocumentParser {

    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5MB Limit

    @Override
    public DocumentContent parse(InputStream inputStream) throws Exception {
        // 1. Read MAX_SIZE + 1 to detect overflow (same as PdfDocumentParser)
        byte[] allBytes = inputStream.readNBytes((int) MAX_SIZE + 1);
        
        if (allBytes.length > MAX_SIZE) {
            throw new DocumentParsingException("DOCX exceeds the allowed 5MB limit");
        }

        // 2. Use try-with-resources with a ByteArrayInputStream
        try (ByteArrayInputStream bais = new ByteArrayInputStream(allBytes);
             XWPFDocument doc = new XWPFDocument(bais)) {
            
            DocumentContent document = new DocumentContent();
            document.setTitle("Parsed DOCX Document");

            DocumentSection currentSection = new DocumentSection("Main", 1);

            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                String text = paragraph.getText();

                if (text == null || text.trim().isEmpty()) {
                    continue;
                }

                currentSection.getContentBlocks()
                        .add(new ParagraphBlock(text.trim()));
            }

            document.getSections().add(currentSection);
            return document;
        }
    }

    @Override
    public boolean supports(String fileType) {
        return fileType != null && 
               (fileType.equalsIgnoreCase("docx") || 
                fileType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }
}