package com.doculatex.backend.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LatexResponseTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        LatexResponse response = new LatexResponse(
                "LaTeX Content",
                "document.pdf",
                now,
                "Successfully generated"
        );

        assertEquals("LaTeX Content", response.getLatexContent());
        assertEquals("document.pdf", response.getOriginalFileName());
        assertEquals(now, response.getGeneratedAt());
        assertEquals("Successfully generated", response.getMessage());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        LatexResponse response = new LatexResponse();
        response.setLatexContent("Test Content");
        response.setOriginalFileName("test.pdf");
        response.setGeneratedAt(now);
        response.setMessage("Test Message");

        assertEquals("Test Content", response.getLatexContent());
        assertEquals("test.pdf", response.getOriginalFileName());
        assertEquals(now, response.getGeneratedAt());
        assertEquals("Test Message", response.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        LatexResponse r1 = new LatexResponse("Content", "file.pdf", now, "Msg");
        LatexResponse r2 = new LatexResponse("Content", "file.pdf", now, "Msg");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();

        LatexResponse response = new LatexResponse("Content", "file.pdf", now, "Msg");
        String result = response.toString();

        assertTrue(result.contains("Content"));
        assertTrue(result.contains("file.pdf"));
    }
}
