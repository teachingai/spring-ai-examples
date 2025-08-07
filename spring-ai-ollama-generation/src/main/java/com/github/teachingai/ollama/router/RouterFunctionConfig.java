package com.github.teachingai.ollama.router;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterFunctionConfig {

    private final OllamaChatModel chatModel;

    @Autowired
    public RouterFunctionConfig(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Bean
    RouterFunction<ServerResponse> routes(OllamaChatModel chatModel) {
        return RouterFunctions.route()

                .GET("/reactive/generate", req -> {
                    String message = req.param("message").orElse("tell me a joke");
                    return ServerResponse.ok().body( Mono.fromCallable(() -> chatModel.call(message))
                    );
                })
                .GET("/reactive/stream", req -> {
                    Prompt prompt = new Prompt(new UserMessage(req.param("message").orElse("Tell me a joke")));
                    return ServerResponse.ok().body(chatModel.stream(prompt).buffer());
                })
                .build();
    }

}
