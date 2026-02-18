package com.doculatex.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // 1. The "Shortcut"
@NoArgsConstructor   // 2. The "Open Door"
@AllArgsConstructor  // 3. The "Full House"
public class ParagraphBlock implements ContentBlock {
    private String text;
}