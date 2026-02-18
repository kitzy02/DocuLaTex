package com.doculatex.backend.parser;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ParserFactory {

    private final List<DocumentParser> parsers;

    // Spring finds every @Component that implements DocumentParser 
    // and puts them in this list automatically! 🪄
    public ParserFactory(List<DocumentParser> parsers) {
        this.parsers = parsers;
    }

    public DocumentParser getParser(String fileType) {
        return parsers.stream()
                .filter(parser -> parser.supports(fileType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported type: " + fileType));
    }
}