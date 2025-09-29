package com.github.teachingai.ollama;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import reactor.core.publisher.Flux;

public class OllamaGenerationTest {

    public static void main(String[] args) {

        /*
         * deepseek-r1:8b ：https://ollama.com/library/deepseek-r1
         * qwen3:8b ：https://ollama.com/library/qwen8
         * gemma3:4b ：https://ollama.com/library/gemma3
         */
        var ollamaApi = OllamaApi.builder().build();
        var ollamaOptions = OllamaOptions.builder()
                .model("gemma3:4b")
                .temperature(0.9d).build();
        var chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(ollamaOptions).build();

        testAsync(chatModel);
        testSync(chatModel);

    }

    private static void testAsync(ChatModel chatModel) {
        Prompt prompt = new Prompt("给我讲个关于【龟兔赛跑】的故事！");
        Flux<ChatResponse> flux = chatModel.stream(prompt);
        flux.doFinally(e -> {
            System.out.println();
        }).toStream().forEach(res -> {
            System.out.print(res.getResult().getOutput().getText());
        });
        System.out.println("==============================================================================================================================");
    }

    private static void testSync(ChatModel chatModel) {
        String resp = chatModel.call("给我推荐一本儿童读物！");
        System.out.println(resp);
        System.out.println("==============================================================================================================================");
    }
}
