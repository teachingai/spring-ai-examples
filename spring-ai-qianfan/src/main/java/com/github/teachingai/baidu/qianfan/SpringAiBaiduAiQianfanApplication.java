package com.github.teachingai.baidu.qianfan;

import org.springframework.ai.chat.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class SpringAiBaiduAiQianfanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiBaiduAiQianfanApplication.class, args);
    }

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
