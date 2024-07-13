package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ApiUtils;
import com.github.teachingai.ollama.api.EdgeTtsNativeAudioApi;
import org.apache.commons.lang3.StringUtils;
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
         * gemma2:9b ：https://ollama.com/library/gemma2
         * glm4:9b ：https://ollama.com/library/glm4
         * llama3:8b ：https://ollama.com/library/llama3
         * mistral ：https://ollama.com/library/mistral
         */
        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi, OllamaOptions.create()
                .withModel("glm4")
                .withTemperature(0.9f));

        var chatTtsApi = new EdgeTtsNativeAudioApi();
        var chatTtsClient = new EdgeTtsNativeAudioSpeechClient(chatTtsApi, EdgeTtsAudioSpeechOptions.builder()
                // `edge-tts -l` 查看可用的声音，
                // 普通话-男：zh-CN-YunyangNeural
                // 普通话-女：zh-CN-XiaoxiaoNeural、zh-CN-XiaoyiNeural
                // 辽宁方言-女：zh-CN-liaoning-XiaobeiNeural
                // 陕西方言-女：zh-CN-shaanxi-XiaoniNeural
                .withVoice("zh-CN-YunyangNeural")
                .withRate("-10%")
                .withVolume("+50%")
                .withOutput("E://edge-tts")
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
                String text = MarkdownUtils.removeMarkdownTags(MarkdownUtils.convertChinesePunctuationToEnglish(resp));
                       text = StringUtils.replace(text, "\"", "");
                       text = StringUtils.replace(text, "'", "");
                InputStream stream = chatTtsClient.call(text);
                if(Objects.nonNull(stream)){
                    System.out.println("<<< 音频开始播放...");
                    AudioPlayer.playMP3(stream);
                    System.out.println(">>> 音频播放完");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
