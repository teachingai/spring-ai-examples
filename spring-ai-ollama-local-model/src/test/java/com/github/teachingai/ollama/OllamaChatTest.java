package com.github.teachingai.ollama;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.Scanner;

public class OllamaChatTest {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi, OllamaOptions.create()
                .withModel("qwen:7b")
                .withFormat("json")
                .withTemperature(0.9f));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">>> ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            String resp = chatClient.call(message);
            System.out.println("<<< " + resp);
        }
    }

}
