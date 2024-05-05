package com.github.hiwepy.ollama.controller;

import com.github.hiwepy.ollama.request.ApiRequest;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private final OllamaChatClient chatClient;

    @Autowired
    public ChatController(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/v1/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/v1/prompt")
    public List<Generation> prompt(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");
        Prompt prompt = promptTemplate.create(Map.of("adjective", "funny", "topic", "cats"));
        return chatClient.call(prompt).getResults();
    }

    @PostMapping("/v1/chat/completions")
    public Flux<ChatResponse> chatCompletions(@RequestBody ApiRequest.ChatCompletionRequest chatRequest) {

        chatRequest.messages().forEach(System.out::println);


        ChatOptions modelOptions = new OllamaOptions()
                .withModel(chatRequest.model())
                .withTemperature(chatRequest.temperature()).withTopP(chatRequest.topP());

        List<Message> messages = chatRequest.messages().stream().map(msg -> {
            switch (msg.role()) {
                case ASSISTANT:
                    return new AssistantMessage(msg.content());
                case SYSTEM:
                    return new SystemMessage(msg.content());
                default:
                    return new UserMessage(msg.content());
            }
        }).collect(Collectors.toList());

        Prompt prompt = new Prompt(messages, modelOptions);
        return chatClient.stream(prompt);
    }

}
