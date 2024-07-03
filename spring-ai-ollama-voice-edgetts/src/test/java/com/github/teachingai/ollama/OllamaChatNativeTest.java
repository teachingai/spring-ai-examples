package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ApiUtils;
import com.github.teachingai.ollama.api.EdgeTtsNativeAudioApi;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class OllamaChatNativeTest {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi, OllamaOptions.create()
                .withModel("qwen2:1.5b")
                .withTemperature(0.9f));

        var chatTtsApi = new EdgeTtsNativeAudioApi();
        var chatTtsClient = new EdgeTtsNativeAudioSpeechClient(chatTtsApi, EdgeTtsAudioSpeechOptions.builder()
                .withVoice(ApiUtils.DEFAULT_VOICE)
                .build());

        Scanner scanner = new Scanner(System.in);
        List<Message> historyList = new ArrayList<>();
        while (true) {
            System.out.print(">>> ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            historyList.add(new UserMessage(message));
            Prompt prompt = new Prompt(historyList);
            ChatResponse chatResponse = chatClient.call(prompt);
            historyList.add(chatResponse.getResult().getOutput());
            String resp = chatResponse.getResult().getOutput().getContent();
            System.out.println("<<< " + resp);
            try {
                System.out.println(">>> 生成音频中...");
                InputStream stream = chatTtsClient.call(MarkdownUtils.removeMarkdownTags(MarkdownUtils.convertChinesePunctuationToEnglish(resp)));
                if(Objects.nonNull(stream)){
                    System.out.println("<<< 音频开始播放...");
                    AudioPlayer.playWav(stream);
                    System.out.println(">>> 音频播放完");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
