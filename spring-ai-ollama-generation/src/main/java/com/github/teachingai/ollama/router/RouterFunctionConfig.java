package com.github.teachingai.ollama.router;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Configuration
public class RouterFunctionConfig {

    private final OllamaChatClient chatClient;

    @Autowired
    public RouterFunctionConfig(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Bean
    RouterFunction<ServerResponse> routes(OllamaChatClient chatClient) {
        return RouterFunctions.route()

                .GET("/reactive/generate", req -> {
                    String message = req.param("message").orElse("tell me a joke");
                    return ServerResponse.ok().body( Mono.fromCallable(() -> chatClient.call(message))
                    );
                })
                .GET("/reactive/stream", req -> {
                    Prompt prompt = new Prompt(new UserMessage(req.param("message").orElse("Tell me a joke")));
                    return ServerResponse.ok().body(
                            Flux.fromIterable(chatClient.stream(prompt))
                                    .delayElements(Duration.ofMillis(100))
                    );
                })
                .build();
    }

}
