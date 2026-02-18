package com.doculatex.backend.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DocumentContent {
    private String title;
    private String author;
    
    // Extra info like "date" or "keywords"
    private Map<String, String> metadata = new HashMap<>();
    
    // The starting list of Chapters
    private List<DocumentSection> sections = new ArrayList<>();
}