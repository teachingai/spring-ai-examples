package com.github.teachingai.ollama;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Ollama_Prompt_Kuakua_Test2 {

    /**
     * qwen2:7b ：https://ollama.com/library/qwen2
     * gemma2:9b ：https://ollama.com/library/gemma2
     * glm4:9b ：https://ollama.com/library/glm4
     * llama3:8b ：https://ollama.com/library/llama3
     * mistral ：https://ollama.com/library/mistral
     */
    public static void main(String[] args) throws IOException {

        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi);

        List<Message> historyList = new ArrayList<>();
        // 系统提示消息

        Resource systemResource = new ClassPathResource("prompts/kuakua-message.st");
        String systemPrompt =  systemResource.getContentAsString(StandardCharsets.UTF_8);
        historyList.add(new SystemMessage(systemPrompt));
        String firstText = "今天工作很累呢～";
        System.out.println("<<< " + firstText);
        // 用户输入消息
        historyList.add(new UserMessage(firstText));
        // 生成对话
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Prompt prompt = new Prompt(historyList, OllamaOptions.create()
                    .withModel("qwen2")
                    .withTemperature(0.7f)
                    .withLowVRAM(Boolean.TRUE)
                    .withSeed(ThreadLocalRandom.current().nextInt())
                );
            Flux<ChatResponse> chatResponse = chatClient.stream(prompt);
            System.out.print(">>> ");
            StringBuilder sb = new StringBuilder();
            chatResponse.doOnNext(response -> {
                historyList.add(response.getResult().getOutput());
                String resp = response.getResult().getOutput().getContent();
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
