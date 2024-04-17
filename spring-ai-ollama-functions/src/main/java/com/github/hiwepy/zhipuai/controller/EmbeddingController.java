package com.github.hiwepy.zhipuai.controller;

import org.springframework.ai.zhipuai.ZhipuAiEmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmbeddingController {

    private final ZhipuAiEmbeddingClient embeddingClient;

    @Autowired
    public EmbeddingController(ZhipuAiEmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @GetMapping("/ai/embedding")
    public Map embedding(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("embeddings", embeddingClient.embed(message));
    }

}
