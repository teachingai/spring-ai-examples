package com.github.teachingai.azure.openai.router;

import org.springframework.ai.chat.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    RouterFunction<ServerResponse> routes(ChatClient chatModel) {
        return RouterFunctions.route()
                .GET("/ask", req ->
                        ServerResponse.ok().body(
                                chatModel.call(req.param("question")
                                        .orElse("tell me a joke"))))
                .build();
    }

}
