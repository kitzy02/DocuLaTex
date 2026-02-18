package com.doculatex.backend.parser;

import com.doculatex.backend.model.DocumentContent;
import java.io.InputStream;

public interface DocumentParser {
    // Every parser must implement these two methods
    DocumentContent parse(InputStream inputStream) throws Exception;
    boolean supports(String fileType);
}