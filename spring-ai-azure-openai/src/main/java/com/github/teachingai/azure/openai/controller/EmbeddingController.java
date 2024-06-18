package com.github.teachingai.azure.openai.controller;

import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmbeddingController {

    private final AzureOpenAiEmbeddingClient embeddingClient;

    @Autowired
    public EmbeddingController(AzureOpenAiEmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @GetMapping("/v1/embedding")
    public Map embedding(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("embeddings", embeddingClient.embed(message));
    }

}
