package com.github.hiwepy.zhipuai.controller;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.zhipuai.ZhipuAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final ZhipuAiChatClient chatClient;

    @Autowired
    public ChatController(ZhipuAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/ai/prompt")
    public List<Generation> prompt(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");
        Prompt prompt = promptTemplate.create(Map.of("adjective", "funny", "topic", "cats"));
        return chatClient.call(prompt).getResults();
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }

}
