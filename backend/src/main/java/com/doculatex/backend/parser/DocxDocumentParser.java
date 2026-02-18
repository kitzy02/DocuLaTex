package com.doculatex.backend.parser;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import com.doculatex.backend.model.ParagraphBlock;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DocxDocumentParser implements DocumentParser {

    @Override
    public DocumentContent parse(InputStream inputStream) throws Exception {

        XWPFDocument doc = new XWPFDocument(inputStream);

        DocumentContent document = new DocumentContent();
        document.setTitle("Parsed DOCX Document");

        DocumentSection currentSection =
                new DocumentSection("Main", 1);

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

    @Override
    public boolean supports(String fileType) {
        return fileType.equalsIgnoreCase("docx");
    }
}
