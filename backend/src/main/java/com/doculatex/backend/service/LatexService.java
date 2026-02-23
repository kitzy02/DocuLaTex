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
            // Escape title text
            latex.append("\\title{").append(escapeLatex(document.getTitle())).append("}\n");
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
        // Escape heading text
        latex.append(command).append(escapeLatex(section.getHeading())).append("}\n");

        // Add the actual content (Paragraphs) 📝
        for (ContentBlock block : section.getContentBlocks()) {
            if (block instanceof ParagraphBlock paragraph) {
                // Escape paragraph text
                latex.append(escapeLatex(paragraph.getText())).append("\n\n");
            }
        }

        // Recursion: Process Subsections inside this section 🔄
        for (DocumentSection sub : section.getSubSections()) {
            appendSection(latex, sub);
        }
    }

    /**
     * Escapes special LaTeX characters to prevent command injection or compilation errors.
     */
    private String escapeLatex(String text) {
        if (text == null) {
            return "";
        }
        // Note: Backslash replacement must happen first!
        return text
                .replace("\\", "\\textbackslash{}")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("$", "\\$")
                .replace("&", "\\&")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("%", "\\%")
                .replace("~", "\\textasciitilde{}")
                .replace("^", "\\textasciicircum{}");
    }
}