package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ApiUtils;
import com.github.teachingai.ollama.api.ChatTtsAudioApi;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;
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
        while (true) {
            System.out.print(">>> ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            String resp = chatClient.call(message);
            System.out.println("<<< " + resp);
            try {
                System.out.print(">>> 开始生成音频...");
                InputStream stream = chatTtsClient.call(resp);
                System.out.println("音频生成完成");
                if(Objects.nonNull(stream)){
                    playWav(stream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void playWav(InputStream wavStream) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioFormat format = clip.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Wrap the InputStream in a BufferedInputStream
            BufferedInputStream bufferedStream = new BufferedInputStream(wavStream);
            bufferedStream.mark(wavStream.available());

            clip = (Clip) AudioSystem.getLine(info);
            clip.open(AudioSystem.getAudioInputStream(bufferedStream));

            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1808);
            clip.drain();
            clip.stop();
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
