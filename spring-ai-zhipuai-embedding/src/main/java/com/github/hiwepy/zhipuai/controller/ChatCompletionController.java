package com.github.hiwepy.zhipuai.controller;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhipuAiChatClient;
import org.springframework.ai.zhipuai.api.ZhipuAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ChatCompletionController {

    private final ZhipuAiChatClient chatClient;

    @Autowired
    public ChatCompletionController(ZhipuAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/v1/chat/completions")
    public Flux<ChatResponse> chatCompletions(@RequestBody ApiRecord.ChatCompletionRequest request) {
        // 1、对Message进行转换
        List<Message> messages = request.messages().stream().map(msg -> {
            switch (msg.role()) {
                case USER:
                    return new UserMessage(msg.content());
                case SYSTEM:
                    return new SystemMessage(msg.content());
                case ASSISTANT:
                    return new AssistantMessage(msg.content());
                default:
                    return new SystemMessage(msg.content());
            }
        }).collect(Collectors.toList());
        // 2、对ChatOptions进行转换
        ChatOptions modelOptions = ZhipuAiChatOptions.builder()
                .withModel(request.model())
                .withMaxTokens(request.maxTokens())
                .withDoSample(request.doSample())
                .withTemperature(request.temperature())
                .withTopP(request.topP())
                .withUser(request.user())
                .withStop(request.stop())
                .build();
        // 3、构建Prompt
        Prompt prompt = new Prompt(messages, modelOptions);
        // 4、调用ChatClient
        if(Objects.nonNull(request.stream()) && Boolean.TRUE.equals(request.stream())){
            return chatClient.stream(prompt);
        }
        return Flux.just(chatClient.call(prompt));
    }

}
