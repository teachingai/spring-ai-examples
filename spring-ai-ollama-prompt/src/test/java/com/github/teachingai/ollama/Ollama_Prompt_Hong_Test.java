package com.github.teachingai.ollama;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
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

/**
 * [哄哄模拟器](https://hong.greatdk.com/)基于 AI 技术，你需要使用语言技巧和沟通能力，在限定次数内让对方原谅你，这并不容易
 * 它的核心技术就是提示工程。著名提示工程师宝玉[复刻了它的提示词](https://weibo.com/1727858283/ND9pOzB0K)：
 */
public class Ollama_Prompt_Hong_Test {

    /**
     * qwen2:7b ：https://ollama.com/library/qwen2
     * gemma2:9b ：https://ollama.com/library/gemma2
     * glm4:9b ：https://ollama.com/library/glm4
     * llama3:8b ：https://ollama.com/library/llama3
     * mistral ：https://ollama.com/library/mistral
     */
    public static void main(String[] args) throws IOException {

        var ollamaApi = new OllamaApi();
        var chatModel = new OllamaChatModel(ollamaApi);

        List<Message> historyList = new ArrayList<>();
        // 系统提示消息
        Resource systemResource = new ClassPathResource("prompts/hong-message.st");
        String systemPrompt =  systemResource.getContentAsString(StandardCharsets.UTF_8);
        historyList.add(new SystemMessage(systemPrompt));
        historyList.add(new UserMessage("因为玩游戏，惹女朋友生气了"));
        // 生成对话
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Prompt prompt = new Prompt(historyList, OllamaOptions.create()
                    .withModel("qwen2-7b-kuakua")
                    .withTemperature(0.7f)
                    .withLowVRAM(Boolean.TRUE)
                    .withSeed(ThreadLocalRandom.current().nextInt())
            );
            Flux<ChatResponse> chatResponse = chatModel.stream(prompt);
            System.out.print(">>> ");
            StringBuilder sb = new StringBuilder();
            chatResponse.doOnNext(response -> {
                historyList.add(response.getResult().getOutput());
                String resp = response.getResult().getOutput().getContent();
                System.out.print(resp);
                sb.append(resp);
            }).blockLast();
            System.out.println();
            System.out.print("<<< ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }

        }
    }

}
