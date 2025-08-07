package com.github.hiwepy.llmsfreeapi.router;

import org.springframework.ai.llmsfreeapi.LLMsFreeApiChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    RouterFunction<ServerResponse> routes(LLMsFreeApiChatClient chatModel) {
        return RouterFunctions.route()
                .GET("/ask", req ->
                        ServerResponse.ok().body(
                                chatModel.call(req.param("question")
                                        .orElse("tell me a joke"))))
                .build();
    }

}
