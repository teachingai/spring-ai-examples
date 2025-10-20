package com.github.teachingai.ollama;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.Scanner;

public class OllamaChatTest {

    public static void main(String[] args) {

         var ollamaApi = OllamaApi.builder().build();
        var chatModel = new OllamaChatModel(ollamaApi)
                .withDefaultOptions(OllamaOptions.create()
                        .withModel("qwen:7b")
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
