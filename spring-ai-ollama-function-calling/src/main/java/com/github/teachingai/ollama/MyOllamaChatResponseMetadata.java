package com.github.teachingai.ollama;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.metadata.OllamaChatResponseMetadata;
import org.springframework.ai.ollama.metadata.OllamaUsage;
import org.springframework.util.Assert;

public class MyOllamaChatResponseMetadata  implements ChatResponseMetadata {

    protected static final String AI_METADATA_STRING = "{ @type: %1$s, usage: %2$s, rateLimit: %3$s }";

    public static MyOllamaChatResponseMetadata from(MyOllamaApi.ChatResponse response) {
        Assert.notNull(response, "MyOllamaApi.ChatResponse must not be null");
        Usage usage = MyOllamaUsage.from(response);
        return new MyOllamaChatResponseMetadata(usage);
    }

    private final Usage usage;

    protected MyOllamaChatResponseMetadata(Usage usage) {
        this.usage = usage;
    }

    @Override
    public Usage getUsage() {
        return this.usage;
    }

    @Override
    public String toString() {
        return AI_METADATA_STRING.formatted(getClass().getTypeName(), getUsage(), getRateLimit());
    }

}
