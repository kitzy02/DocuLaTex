package com.doculatex.backend.controller;

import com.doculatex.backend.dto.LatexResponse;
import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.service.DocumentService;
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

    @PostMapping("/upload")
    public ResponseEntity<LatexResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        // 1️⃣ Call service
        DocumentContent content = documentService.parseDocument(file);

        // 2️⃣ Convert to DTO
        LatexResponse response = new LatexResponse(
                content.toString(), // Replace later with real LaTeX generator
                file.getOriginalFilename(),
                LocalDateTime.now(),
                "Document parsed successfully"
        );

        // 3️⃣ Return
        return ResponseEntity.ok(response);
    }
}