package com.github.teachingai.ollama.controller;

import com.github.teachingai.ollama.request.ApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ChatController {

    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("/v1/chat/completions")
    public Flux<ChatResponse> chatCompletions(@RequestBody ApiRequest.ChatCompletionRequest chatRequest) {
        // 打印请求消息
        chatRequest.messages().forEach(item > log.info("{}", item));
        // 构建模型选项
        ChatOptions modelOptions = new OllamaOptions.Builder()
                .model(chatRequest.model())
                .temperature(chatRequest.temperature())
                .topP(chatRequest.topP())
                .build();
        // 构建消息列表
        List<Message> messages = chatRequest.messages().stream().map(msg -> switch (msg.role()) {
            case ASSISTANT -> new AssistantMessage(msg.content());
            case SYSTEM -> new SystemMessage(msg.content());
            default -> new UserMessage(msg.content());
        }).collect(Collectors.toList());

        Prompt prompt = new Prompt(messages, modelOptions);
        return chatModel.stream(prompt);
    }

}
