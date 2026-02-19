package com.doculatex.backend.service;

import com.doculatex.backend.model.*;
import org.springframework.stereotype.Service;

@Service
public class LatexService {

    public String generateLatex(DocumentContent document) {
        StringBuilder latex = new StringBuilder();

        // 1. Setup the Document Header 📄
        latex.append("\\documentclass{article}\n");
        latex.append("\\begin{document}\n");

        if (document.getTitle() != null) {
            latex.append("\\title{").append(document.getTitle()).append("}\n");
            latex.append("\\maketitle\n");
        }

        // 2. Process Sections 📂
        for (DocumentSection section : document.getSections()) {
            appendSection(latex, section);
        }

        // 3. Close the Document 🏁
        latex.append("\\end{document}");
        return latex.toString();
    }

    private void appendSection(StringBuilder latex, DocumentSection section) {
        // Determine the LaTeX command based on the nesting level
        String command = (section.getLevel() == 1) ? "\\section{" : "\\subsection{";
        latex.append(command).append(section.getHeading()).append("}\n");

        // Add the actual content (Paragraphs) 📝
        for (ContentBlock block : section.getContentBlocks()) {
            if (block instanceof ParagraphBlock paragraph) {
                latex.append(paragraph.getText()).append("\n\n");
            }
        }

        // Recursion: Process Subsections inside this section 🔄
        for (DocumentSection sub : section.getSubSections()) {
            appendSection(latex, sub);
        }
    }
}