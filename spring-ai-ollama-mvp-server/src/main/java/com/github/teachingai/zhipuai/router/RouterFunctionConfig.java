package com.github.teachingai.zhipuai.router;

import org.springframework.ai.zhipuai.ZhipuAiChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ZhipuAiChatClient chatClient) {
        return RouterFunctions.route()
                .GET("/route/v1/generate", req ->
                        ServerResponse.ok().body(
                                chatClient.call(req.param("question")
                                        .orElse("tell me a joke"))))
                .GET("/route/v1/prompt", req ->
                        ServerResponse.ok().body(
                                chatClient.call(req.param("question")
                                        .orElse("tell me a joke"))))
                .GET("/route/v1/chat/completions", req ->
                        ServerResponse.ok().body(
                                chatClient.call(req.param("question")
                                        .orElse("tell me a joke"))))
                .build();
    }

}
