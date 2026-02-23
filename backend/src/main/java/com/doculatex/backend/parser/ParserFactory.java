package com.doculatex.backend.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParserFactory {

    private final List<DocumentParser> parsers;

    public DocumentParser getParser(String fileType) {
        if (fileType == null || fileType.isBlank()) {
            throw new IllegalArgumentException("File type is required");
        }

        return parsers.stream()
                .filter(parser -> parser.supports(fileType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported type: " + fileType));
    }
}