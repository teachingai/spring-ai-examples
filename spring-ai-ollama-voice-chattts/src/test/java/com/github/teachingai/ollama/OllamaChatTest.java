package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ApiUtils;
import com.github.teachingai.ollama.api.ChatTtsAudioApi;
import javazoom.jl.player.Player;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class OllamaChatTest {

    public static void main(String[] args) {

        /**
         * qwen2:7b ：https://ollama.com/library/qwen2
         * gemma2 ：https://ollama.com/library/gemma2
         * llama3 ：https://ollama.com/library/llama3
         * mistral ：https://ollama.com/library/mistral
         */
         var ollamaApi = OllamaApi.builder().build();
        var chatModel = new OllamaChatModel(ollamaApi, OllamaOptions.create()
                .withModel("qwen2:1.5b")
                .withTemperature(0.9f));

        var chatTtsApi = new ChatTtsAudioApi();
        var chatTtsClient = new ChatTtsAudioSpeechClient(chatTtsApi, ChatTtsAudioSpeechOptions.builder()
                .withPrompt("[oral_2][laugh_0][break_6]")
                .withTemperature(ApiUtils.DEFAULT_TEMPERATURE)
                .withTopP(ApiUtils.DEFAULT_TOP_P)
                .withTopK(ApiUtils.DEFAULT_TOP_K)
                .withMaxInferTokens(ApiUtils.DEFAULT_MAX_INFER_TOKENS)
                .withMaxRefineTokens(ApiUtils.DEFAULT_MAX_REFINE_TOKENS)
                .withSpeed(ApiUtils.DEFAULT_SPEED)
                .withTextSeed(ApiUtils.DEFAULT_TEXT_SEED)
                .withCustomVoice(0)
                .withSkipRefine(0)
                .withStream(0)
                .withVoice(ChatTtsAudioApi.SpeechRequest.Voice.VOICE_SEED_1983_RESTORED_EMB.getValue())
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
            ChatResponse chatResponse = chatModel.call(prompt);
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
