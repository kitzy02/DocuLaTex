package com.doculatex.backend.service;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.model.DocumentSection;
import com.doculatex.backend.model.ParagraphBlock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LatexServiceTest {

    private final LatexService latexService = new LatexService();

    @Test
    void shouldGenerateLatexWithTitle() {

        DocumentContent doc = new DocumentContent();
        doc.setTitle("My Title");

        DocumentSection section =
                new DocumentSection("Intro", 1);

        section.getContentBlocks()
                .add(new ParagraphBlock("Hello world"));

        doc.getSections().add(section);

        String latex = latexService.generateLatex(doc);

        assertTrue(latex.contains("\\title{My Title}"));
        assertTrue(latex.contains("\\section{Intro}"));
        assertTrue(latex.contains("Hello world"));
    }

    @Test
    void shouldGenerateLatexWithoutTitle() {

        DocumentContent doc = new DocumentContent();

        String latex = latexService.generateLatex(doc);

        assertFalse(latex.contains("\\title{"));
    }
}
