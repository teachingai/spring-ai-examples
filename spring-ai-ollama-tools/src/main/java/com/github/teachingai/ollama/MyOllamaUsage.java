package com.github.teachingai.ollama;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.util.Assert;

import java.util.Optional;

public class MyOllamaUsage implements Usage {

    protected static final String AI_USAGE_STRING = "{ promptTokens: %1$d, generationTokens: %2$d, totalTokens: %3$d }";

    public static MyOllamaUsage from(MyOllamaApi.ChatResponse response) {
        Assert.notNull(response, "OllamaApi.ChatResponse must not be null");
        return new MyOllamaUsage(response);
    }

    private final MyOllamaApi.ChatResponse response;

    public MyOllamaUsage(MyOllamaApi.ChatResponse response) {
        this.response = response;
    }

    @Override
    public Long getPromptTokens() {
        return Optional.ofNullable(response.promptEvalCount()).map(Integer::longValue).orElse(0L);
    }

    @Override
    public Long getGenerationTokens() {
        return Optional.ofNullable(response.evalCount()).map(Integer::longValue).orElse(0L);
    }

    @Override
    public String toString() {
        return AI_USAGE_STRING.formatted(getPromptTokens(), getGenerationTokens(), getTotalTokens());
    }

}
