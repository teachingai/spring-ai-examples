package com.github.teachingai.ollama.controller;

import com.github.teachingai.ollama.service.IEmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class EmbeddingController {

    private final IEmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(IEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/v1/embedding")
    public Map embedding(@RequestParam(value = "text") String text) {
        return Map.of("embeddings", embeddingService.embedding(text));
    }

    @GetMapping("/v1/embedding")
    public Map embedding(@RequestParam(value = "file") MultipartFile file) {
        return Map.of("embeddings", embeddingService.embedding(file));
    }

}
