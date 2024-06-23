package com.github.teachingai.ollama;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.List;
import java.util.Scanner;

public class OllamaPrompt_Test1 {

    public static void main(String[] args) {

        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi);

        String promptStr = "你的任务是识别用户对手机流量套餐产品的选择条件。\n" +
                "每种流量套餐产品包含三个属性：名称，月费价格，月流量。\n" +
                "根据用户输入，识别用户在上述三种属性上的倾向。";

        List<Message> messages  = List.of(new SystemMessage(promptStr),
                new UserMessage("办个100G的套餐。"));

        Prompt prompt = new Prompt(messages, OllamaOptions.create()
                .withModel("qwen:7b")
                .withTemperature(0f));

        ChatResponse resp = chatClient.call(prompt);

        System.out.println("<<< " + resp.getResults());

    }

}
