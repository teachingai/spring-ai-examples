package com.github.teachingai.ollama;

import com.github.teachingai.ollama.api.ChatTtsAudioApi;
import com.github.teachingai.ollama.api.ChatTtsResponseHeaderExtractor;
import com.github.teachingai.ollama.api.common.OllamaApiException;
import com.github.teachingai.ollama.audio.speech.*;
import com.github.teachingai.ollama.metadata.audio.ChatTtsAudioSpeechResponseMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.time.Duration;

public class ChatTtsAudioSpeechClient implements SpeechClient, StreamingSpeechClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChatTtsAudioSpeechOptions defaultOptions;

    private static final Integer SPEED = 1;

    public final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(10)
            .retryOn(OllamaApiException.class)
            .exponentialBackoff(Duration.ofMillis(2000), 5, Duration.ofMillis(3 * 60000))
            .build();

    private final ChatTtsAudioApi audioApi;

    /**
     * Initializes a new instance of the ChatTtsAudioSpeechClient class with the provided
     * ChatTtsAudioApi. It uses the model chatTTS, response format mp3, voice alloy, and the
     * default speed of 1.0.
     *
     * @param audioApi The ChatTtsAudioApi to use for speech synthesis.
     */
    public ChatTtsAudioSpeechClient(ChatTtsAudioApi audioApi) {
        this(audioApi,
                ChatTtsAudioSpeechOptions.builder()
                        .withTemperature(0.7F)
                        .withTopP(0.7F)
                        .withTopK(20)
                        .withTextSeed(42F)
                        .withVoice(ChatTtsAudioApi.SpeechRequest.Voice.VOICE_SEED_1983_RESTORED_EMB.getValue())
                        .withSpeed(SPEED)
                        .build());
    }

    /**
     * Initializes a new instance of the ChatTtsAudioSpeechClient class with the provided
     * ChatTtsAudioApi and options.
     *
     * @param audioApi The ChatTtsAudioApi to use for speech synthesis.
     * @param options  The ChatTtsAudioSpeechOptions containing the speech synthesis
     *                 options.
     */
    public ChatTtsAudioSpeechClient(ChatTtsAudioApi audioApi, ChatTtsAudioSpeechOptions options) {
        Assert.notNull(audioApi, "ChatTtsAudioApi must not be null");
        Assert.notNull(options, "ChatTtsAudioSpeechOptions must not be null");
        this.audioApi = audioApi;
        this.defaultOptions = options;
    }

    @Override
    public InputStream call(String text) {
        SpeechPrompt speechRequest = new SpeechPrompt(text);
        return call(speechRequest).getResult().getOutput();
    }

    @Override
    public SpeechResponse call(SpeechPrompt speechPrompt) {

        return this.retryTemplate.execute(ctx -> {

            ChatTtsAudioApi.SpeechRequest speechRequest = this.createRequestBody(speechPrompt);

            ResponseEntity<ChatTtsAudioApi.SpeechResponse> speechEntity = this.audioApi.createSpeech(speechRequest);

            var speech = speechEntity.getBody();

            if (speech == null) {
                logger.warn("No speech response returned for speechRequest: {}", speechRequest);
                return new SpeechResponse(new Speech());
            }

            RateLimit rateLimits = ChatTtsResponseHeaderExtractor.extractAiResponseHeaders(speechEntity);

            return new SpeechResponse(new Speech(speech.filename(), speech.url()), new ChatTtsAudioSpeechResponseMetadata(rateLimits));

        });
    }

    /**
     * Streams the audio response for the given speech prompt.
     *
     * @param prompt The speech prompt containing the text and options for speech
     *               synthesis.
     * @return A Flux of SpeechResponse objects containing the streamed audio and
     * metadata.
     */
    @Override
    public Flux<SpeechResponse> stream(SpeechPrompt prompt) {
        return this.audioApi.stream(this.createRequestBody(prompt))
                .map(entity -> {

                    var speech = entity.getBody();

                    if (speech == null) {
                        logger.warn("No speech response returned for prompt: {}", prompt);
                        return new SpeechResponse(new Speech());
                    }
                    return new SpeechResponse(new Speech(speech.filename(), speech.url()), new ChatTtsAudioSpeechResponseMetadata());
                });
    }

    private ChatTtsAudioApi.SpeechRequest createRequestBody(SpeechPrompt prompt) {
        ChatTtsAudioSpeechOptions options = this.defaultOptions;

        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof ChatTtsAudioSpeechOptions runtimeOptions) {
                options = this.merge(options, runtimeOptions);

            } else {
                throw new IllegalArgumentException("Prompt options are not of type SpeechOptions: "
                        + prompt.getOptions().getClass().getSimpleName());
            }
        }

        // runtime options
        ModelOptions runtimeOptions = null;
        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof ChatTtsAudioSpeechOptions runtimeSpeechOptions) {
                runtimeOptions = ModelOptionsUtils.copyToTarget(runtimeSpeechOptions, ModelOptions.class, ChatTtsAudioSpeechOptions.class);
            }
            else {
                throw new IllegalArgumentException("Prompt options are not of type SpeechOptions: "
                        + prompt.getOptions().getClass().getSimpleName());
            }
        }

        ChatTtsAudioSpeechOptions mergedOptions = ModelOptionsUtils.merge(runtimeOptions, this.defaultOptions, ChatTtsAudioSpeechOptions.class);

        String input = StringUtils.isNotBlank(options.getText()) ? options.getText() : prompt.getInstructions().getText();

        ChatTtsAudioApi.SpeechRequest.Builder requestBuilder = ChatTtsAudioApi.SpeechRequest.builder()
                .withText(input)
                .withPrompt(mergedOptions.getPrompt())
                .withVoice(mergedOptions.getVoice())
                .withSpeed(mergedOptions.getSpeed())
                .withTemperature(mergedOptions.getTemperature())
                .withTopP(mergedOptions.getTopP())
                .withTopK(mergedOptions.getTopK())
                .withMaxRefineTokens(mergedOptions.getMaxRefineTokens())
                .withMaxInferTokens(mergedOptions.getMaxInferTokens())
                .withTextSeed(mergedOptions.getTextSeed())
                .withSkipRefine(mergedOptions.getSkipRefine())
                .withStream(mergedOptions.getStream())
                .withCustomVoice(options.getCustomVoice());

        return requestBuilder.build();
    }

    private ChatTtsAudioSpeechOptions merge(ChatTtsAudioSpeechOptions source, ChatTtsAudioSpeechOptions target) {
        ChatTtsAudioSpeechOptions.Builder mergedBuilder = ChatTtsAudioSpeechOptions.builder();

        mergedBuilder.withText(source.getText() != null ? source.getText() : target.getText());
        mergedBuilder.withVoice(source.getVoice() != null ? source.getVoice() : target.getVoice());
        mergedBuilder.withSpeed(source.getSpeed() != null ? source.getSpeed() : target.getSpeed());

        return mergedBuilder.build();
    }
}
