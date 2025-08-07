package com.github.teachingai.ollama;

import java.util.Scanner;

public class OllamaFunctionCallingTest1 {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new MyOllamaApi();
        var chatModel = new MyOllamaChatClient(ollamaApi, OllamaChatOptions.builder()
                .withModel("qwen:7b")
                .withFormat("json")
                .withFunction("CurrentWeather")
                .withTemperature(0.9f)
                .build());

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
