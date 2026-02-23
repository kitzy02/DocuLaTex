package com.doculatex.backend.controller;

import com.doculatex.backend.dto.LatexResponse;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

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
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<LatexResponse> uploadDocument(
            @RequestPart("file") MultipartFile file) {

        // 1️⃣ Call service
        DocumentContent content = documentService.parseDocument(file);

        // 2️⃣ Convert to DTO
        LatexResponse response = new LatexResponse(
                content.toString(),   // Later replace with real LaTeX generator
                file.getOriginalFilename(),
                LocalDateTime.now(),
                "Document parsed successfully"
        );

        // 3️⃣ Return
        return ResponseEntity.ok(response);
    }
}