package com.doculatex.backend.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// 1. The "Identify" Rule
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
// 2. The "Mapping" Registry
@JsonSubTypes({
    @JsonSubTypes.Type(value = ParagraphBlock.class, name = "paragraph")
})
public interface ContentBlock {
}