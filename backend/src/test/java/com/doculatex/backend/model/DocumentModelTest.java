package com.doculatex.backend.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentModelTest {

    @Test
    void testSerializationAndDeserialization() throws Exception {

        // Create paragraph
        ParagraphBlock paragraph = new ParagraphBlock("This is a test paragraph.");

        // Create section
        DocumentSection section = new DocumentSection("Introduction", 1);
        section.getContentBlocks().add(paragraph);

        // Create document
        DocumentContent document = new DocumentContent();
        document.setTitle("Test Document");
        document.setAuthor("John Doe");
        document.getSections().add(section);

        // ObjectMapper for JSON conversion
        ObjectMapper mapper = new ObjectMapper();

        // Serialize to JSON
        String json = mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(document);

        System.out.println("Serialized JSON:");
        System.out.println(json);

        // Deserialize back
        DocumentContent restored =
                mapper.readValue(json, DocumentContent.class);

        // Assertions
        assertEquals("Test Document", restored.getTitle());
        assertEquals(1, restored.getSections().size());
        assertEquals("Introduction",
                     restored.getSections().get(0).getHeading());
    }
}
