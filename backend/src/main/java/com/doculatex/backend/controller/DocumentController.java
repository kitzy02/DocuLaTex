package com.doculatex.backend.controller;

import com.doculatex.backend.dto.LatexResponse;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.service.DocumentService;
import com.doculatex.backend.service.LatexService; // 1. Added Import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final LatexService latexService; 

    @Operation(
        summary = "Upload a document",
        description = "Uploads a PDF or DOCX file and returns parsed LaTeX output",
        responses = {
            @ApiResponse(responseCode = "200", description = "Document parsed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or empty file"),
            @ApiResponse(responseCode = "413", description = "File too large"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping("/upload")
    public ResponseEntity<LatexResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        // 1️⃣ Parse the document into our internal model
        DocumentContent content = documentService.parseDocument(file);

        // 2️⃣ Use LatexService to convert model to LaTeX string
        String latexContent = latexService.generateLatex(content);

        // 3️⃣ Convert to DTO
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            originalFileName = "unknown";
        }
        LatexResponse response = new LatexResponse(
                latexContent, 
                originalFileName,
                LocalDateTime.now(),
                "Document parsed successfully"
        );
        return ResponseEntity.ok(response);
    }
}