package com.doculatex.backend.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentSection {
    private String heading; // e.g., "Introduction"
    private int level;      // 1 for Chapter, 2 for Section, 3 for Sub-section
    
    // The "Leafs": The actual text/lists
    private List<ContentBlock> contentBlocks = new ArrayList<>();
    
    // The "Branches": Smaller sections inside this one
    private List<DocumentSection> subSections = new ArrayList<>();

    public DocumentSection() {}

    public DocumentSection(String heading, int level) {
        this.heading = heading;
        this.level = level;
    }
}