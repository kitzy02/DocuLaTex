package com.doculatex.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data                // Generates Getters, Setters, toString, etc.
@NoArgsConstructor   // Required for JSON libraries
@AllArgsConstructor  // Generates the constructor with all fields
public class LatexResponse {
    private String latexContent;
    private String originalFileName;
    private LocalDateTime generatedAt;
    private String message;
}