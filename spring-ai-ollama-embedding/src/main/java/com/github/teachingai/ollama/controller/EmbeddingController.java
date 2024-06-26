package com.github.teachingai.ollama.controller;

import com.github.teachingai.ollama.service.IEmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
        return embeddingService.embedding(text);
    }

    @PostMapping("/v1/embedding")
    public List<Map> embedding(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return embeddingService.embedding(file);
    }

}
