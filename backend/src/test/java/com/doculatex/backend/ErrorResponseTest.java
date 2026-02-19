package com.doculatex.backend.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse error = new ErrorResponse(
                now,
                400,
                "Bad Request",
                "Invalid file format",
                "/api/upload"
        );

        assertEquals(now, error.getTimestamp());
        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertEquals("Invalid file format", error.getMessage());
        assertEquals("/api/upload", error.getPath());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(now);
        error.setStatus(500);
        error.setError("Internal Server Error");
        error.setMessage("Unexpected failure");
        error.setPath("/api/test");

        assertEquals(now, error.getTimestamp());
        assertEquals(500, error.getStatus());
        assertEquals("Internal Server Error", error.getError());
        assertEquals("Unexpected failure", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse e1 = new ErrorResponse(now, 404, "Not Found", "Missing", "/api");
        ErrorResponse e2 = new ErrorResponse(now, 404, "Not Found", "Missing", "/api");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse error = new ErrorResponse(now, 400, "Bad Request", "Invalid", "/api");
        String result = error.toString();

        assertTrue(result.contains("Bad Request"));
        assertTrue(result.contains("Invalid"));
    }
}
