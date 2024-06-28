package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ChatTtsAudioApi;
import com.github.teachingai.ollama.audio.speech.SpeechPrompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

public class OllamaChatTest {

    public static void main(String[] args) {

        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi)
                .withDefaultOptions(OllamaOptions.create()
                        .withModel("qwen2:1.5b")
                        .withTemperature(0.9f));

        var chatTtsApi = new ChatTtsAudioApi();
        var chatTtsClient = new ChatTtsAudioSpeechClient(chatTtsApi);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">>> ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            String resp = chatClient.call(message);
            System.out.println("<<< " + resp);
            try {
                SpeechPrompt speechPrompt = new SpeechPrompt(resp);
                InputStream stream = chatTtsClient.call(resp);
                if(Objects.nonNull(stream)){
                    MusicPlayer.playMusic(stream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
