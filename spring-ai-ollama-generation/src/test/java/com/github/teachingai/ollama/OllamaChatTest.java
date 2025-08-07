package com.github.teachingai.ollama;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.Scanner;

public class OllamaChatTest {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * gemma2:9b ：https://ollama.com/library/gemma2
         * glm4:9b ：https://ollama.com/library/glm4
         * llama3:8b ：https://ollama.com/library/llama3
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new OllamaApi();
        var chatModel = new OllamaChatModel(ollamaApi, OllamaOptions.create()
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
            String resp = chatModel.call(message);
            System.out.println("<<< " + resp);
        }
    }

}
