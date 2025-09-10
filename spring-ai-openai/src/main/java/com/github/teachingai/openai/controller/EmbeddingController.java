package com.github.teachingai.openai.controller;

import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmbeddingController {

    private final OpenAiEmbeddingModel embeddingModel;

    @Autowired
    public EmbeddingController(OpenAiEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/v1/embedding")
    public Map<String, Object> embedding(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("embeddings", embeddingModel.embed(message));
    }

}
