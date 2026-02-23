package com.doculatex.backend.controller;

import com.doculatex.backend.model.DocumentContent;
import com.doculatex.backend.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    void shouldUploadDocumentSuccessfully() throws Exception {

        DocumentContent mockContent = new DocumentContent();
        mockContent.setTitle("Parsed PDF");

        Mockito.when(documentService.parseDocument(any()))
                .thenReturn(mockContent);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Dummy content".getBytes()
        );

        mockMvc.perform(multipart("/api/documents/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalFileName").value("test.pdf"))
                .andExpect(jsonPath("$.message")
                        .value("Document parsed successfully"));
    }
}