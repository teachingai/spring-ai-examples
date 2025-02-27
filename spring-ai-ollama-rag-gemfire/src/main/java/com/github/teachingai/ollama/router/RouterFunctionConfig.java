package com.github.teachingai.ollama.router;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    RouterFunction<ServerResponse> routes(OllamaChatClient chatClient) {
        return RouterFunctions.route()
                .GET("/generate", req ->
                        ServerResponse.ok().body(
                                chatClient.call(req.param("message")
                                        .orElse("tell me a joke"))))
                .GET("/prompt", req -> {
                    PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");
                    Prompt prompt = new Prompt(new UserMessage(req.param("prompt").orElse("Tell me a joke")));
                    return ServerResponse.ok().body( chatClient.call(prompt));
                })
                .GET("/chat", req -> {
                    Prompt prompt = new Prompt(new UserMessage(req.param("message").orElse("Tell me a joke")));
                    return ServerResponse.ok().body(chatClient.stream(prompt));
                })
                .build();
    }

}
