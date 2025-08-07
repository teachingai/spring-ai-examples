package com.github.teachingai.openai.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class ChatController {

    private final OpenAiChatModel chatModel;

    @Autowired
    public ChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 文本生成
     * @param message 提示词
     * @return 响应结果
     */
    @GetMapping("/ai/generate")
    @Operation(summary = "文本生成")
    public Map<String, Object> generate(@RequestParam(value = "message", defaultValue = "你好！") String message) {
        try {
            String response = chatModel.call(message);
            return Map.of(
                "success", true,
                "generation", response,
                "message", "Generated successfully"
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage(),
                "message", "Generation failed"
            );
        }
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

}
