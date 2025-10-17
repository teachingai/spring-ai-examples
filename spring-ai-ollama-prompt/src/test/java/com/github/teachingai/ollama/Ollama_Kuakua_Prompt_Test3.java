package com.github.teachingai.ollama;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于 自定义提示词 模型的测试
 */
public class Ollama_Kuakua_Prompt_Test3 {

    public static void main(String[] args) throws IOException {

        /*
         * deepseek-r1:8b ：https://ollama.com/library/deepseek-r1
         * qwen3:8b ：https://ollama.com/library/qwen8
         * gemma3:4b ：https://ollama.com/library/gemma3
         */
        var ollamaApi = OllamaApi.builder().build();
        var ollamaOptions = OllamaOptions.builder()
                .model("qwen3:8b")
                .format("json")
                .temperature(0.9d).build();
        var chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(ollamaOptions).build();

        List<Message> historyList = new ArrayList<>();
        String firstText = "今天工作很累呢～";
        System.out.println("<<< " + firstText);
        // 用户输入消息
        historyList.add(new UserMessage(firstText));
        // 生成对话
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Prompt prompt = new Prompt(historyList, OllamaOptions.builder()
                    .model("qwen3:8b-kuakua")
                    .temperature(0.7d)
                    .lowVRAM(Boolean.TRUE)
                    .seed(ThreadLocalRandom.current().nextInt()).build()
                );
            Flux<ChatResponse> chatResponse = chatModel.stream(prompt);
            System.out.print(">>> ");
            StringBuilder sb = new StringBuilder();
            chatResponse.doOnNext(response -> {
                historyList.add(response.getResult().getOutput());
                String resp = response.getResult().getOutput().getText();
                System.out.print(resp);
                sb.append(resp);
            }).blockLast();
            historyList.add(new AssistantMessage(sb.toString()));
            System.out.println("");
            System.out.print("<<< ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            historyList.add(new UserMessage(message));
        }
    }

}
