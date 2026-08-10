package com.github.partmeai.ollama;

import com.github.partmeai.ollama.api.ApiUtils;
import com.github.partmeai.ollama.api.EdgeTtsNativeAudioApi;
import com.github.partmeai.ollama.api.common.OllamaApiException;
import com.github.partmeai.ollama.audio.speech.Speech;
import com.github.partmeai.ollama.audio.speech.SpeechClient;
import com.github.partmeai.ollama.audio.speech.SpeechPrompt;
import com.github.partmeai.ollama.audio.speech.SpeechResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.time.Duration;

public class EdgeTtsNativeAudioSpeechClient implements SpeechClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EdgeTtsAudioSpeechOptions defaultOptions;

    public final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(10)
            .retryOn(OllamaApiException.class)
            .exponentialBackoff(Duration.ofMillis(2000), 5, Duration.ofMillis(3 * 60000))
            .build();

    private final EdgeTtsNativeAudioApi audioApi;

    /**
     * Initializes a new instance of the EdgeTtsAudioSpeechClient class with the provided
     * EdgeTtsAudioApi. It uses the model Edge-TTS, response format mp3, voice alloy, and the
     * default speed of 1.0.
     *
     * @param audioApi The EdgeTtsAudioApi to use for speech synthesis.
     */
    public EdgeTtsNativeAudioSpeechClient(EdgeTtsNativeAudioApi audioApi) {
        this(audioApi, EdgeTtsAudioSpeechOptions.builder()
                        .withVoice(ApiUtils.DEFAULT_VOICE)
                        .build());
    }

    /**
     * Initializes a new instance of the EdgeTtsAudioSpeechClient class with the provided
     * EdgeTtsAudioApi and options.
     *
     * @param audioApi The EdgeTtsAudioApi to use for speech synthesis.
     * @param options  The EdgeTtsAudioSpeechOptions containing the speech synthesis
     *                 options.
     */
    public EdgeTtsNativeAudioSpeechClient(EdgeTtsNativeAudioApi audioApi, EdgeTtsAudioSpeechOptions options) {
        Assert.notNull(audioApi, "EdgeTtsNativeAudioApi must not be null");
        Assert.notNull(options, "EdgeTtsAudioSpeechOptions must not be null");
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

            EdgeTtsNativeAudioApi.SpeechRequest speechRequest = this.createRequestBody(speechPrompt);
            EdgeTtsNativeAudioApi.SpeechResponse speechResponse = this.audioApi.createSpeech(speechRequest);

            if (speechResponse == null) {
                logger.warn("No speech response returned for speechRequest: {}", speechRequest);
                return new SpeechResponse(new Speech());
            }
            logger.info("Speech response: {}", speechResponse);
            if(speechResponse.code() == 1){
                logger.error("Error response returned, Code: {}, Msg: {}", speechResponse.code(), speechResponse.msg());
                throw new OllamaApiException("Error response returned, Code: " + speechResponse.code() + ", Msg: " + speechResponse.msg());
            }

            return new SpeechResponse(new Speech(speechResponse.filename(), null));

        });
    }

    private EdgeTtsNativeAudioApi.SpeechRequest createRequestBody(SpeechPrompt prompt) {

        String input = prompt.getInstructions().getText();
        EdgeTtsAudioSpeechOptions options = this.defaultOptions;

        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof EdgeTtsAudioSpeechOptions runtimeOptions) {
                options = merge(runtimeOptions, options);
            }
            else {
                throw new IllegalArgumentException("Prompt options are not of type SpeechOptions: "
                        + prompt.getOptions().getClass().getSimpleName());
            }
        }


        return new EdgeTtsNativeAudioApi.SpeechRequest(input, options.getVoice(), options.getRate(), options.getVolume(),
                options.getPitch(), options.getWordsInCue(), options.getWriteSubtitles(), options.getProxy(),
                options.getOutput());

    }

    private EdgeTtsAudioSpeechOptions merge(EdgeTtsAudioSpeechOptions source, EdgeTtsAudioSpeechOptions target) {
        return EdgeTtsAudioSpeechOptions.builder()
                .withText(source.getText() != null ? source.getText() : target.getText())
                .withVoice(source.getVoice() != null ? source.getVoice() : target.getVoice())
                .withRate(source.getRate() != null ? source.getRate() : target.getRate())
                .withVolume(source.getVolume() != null ? source.getVolume() : target.getVolume())
                .withPitch(source.getPitch() != null ? source.getPitch() : target.getPitch())
                .withWordsInCue(source.getWordsInCue() != null ? source.getWordsInCue() : target.getWordsInCue())
                .withWriteSubtitles(source.getWriteSubtitles() != null ? source.getWriteSubtitles() : target.getWriteSubtitles())
                .withProxy(source.getProxy() != null ? source.getProxy() : target.getProxy())
                .withOutput(source.getOutput() != null ? source.getOutput() : target.getOutput())
                .build();
    }

}
