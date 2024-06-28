package com.github.teachingai.ollama.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class ChatTtsAudioApi {

    private final RestClient restClient;

    private final WebClient webClient;

    /**
     * Create an new audio api.
     */
    public ChatTtsAudioApi() {
        this(ApiUtils.DEFAULT_BASE_URL, RestClient.builder(), WebClient.builder(),
                RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
    }

    /**
     * Create an new chat completion api.
     * @param baseUrl api base URL.
     * @param restClientBuilder RestClient builder.
     * @param responseErrorHandler Response error handler.
     */
    public ChatTtsAudioApi(String baseUrl,
                           RestClient.Builder restClientBuilder,
                           ResponseErrorHandler responseErrorHandler) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).defaultStatusHandler(responseErrorHandler).build();
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Create an new chat completion api.
     * @param baseUrl api base URL.
     * @param restClientBuilder RestClient builder.
     * @param webClientBuilder WebClient builder.
     * @param responseErrorHandler Response error handler.
     */
    public ChatTtsAudioApi(String baseUrl,
                           RestClient.Builder restClientBuilder,
                           WebClient.Builder webClientBuilder,
                           ResponseErrorHandler responseErrorHandler) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).defaultStatusHandler(responseErrorHandler).build();
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Request to generates audio from the input text.
     * @param text The input text to convert to speech.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SpeechRequest(
            @JsonProperty("text") String text,
            @JsonProperty("prompt") String prompt,
            @JsonProperty("voice") String voice,
            @JsonProperty("speed") Integer speed,
            @JsonProperty("temperature") Float temperature,
            @JsonProperty("top_p") Float topP,
            @JsonProperty("top_k") Integer topK,
            @JsonProperty("refine_max_new_token") Integer maxRefineTokens,
            @JsonProperty("infer_max_new_token") Integer maxInferTokens,
            @JsonProperty("text_seed") Float textSeed,
            @JsonProperty("skip_refine") Integer skipRefine,
            @JsonProperty("is_stream") Integer stream,
            @JsonProperty("custom_voice") Integer customVoice) {

        public SpeechRequest(String text, String prompt, String voice, Integer speed, Float temperature, Float topP, Integer topK,
                             Integer maxRefineTokens, Integer maxInferTokens, Float textSeed, Integer skipRefine, Integer stream,
                             Integer customVoice) {
            Assert.hasText(text, "text must not be empty");
            Assert.hasText(voice, "voice must not be empty");
            Assert.notNull(speed, "speed must not be null");
            Assert.notNull(temperature, "temperature must not be null");
            Assert.notNull(topP, "topP must not be null");
            Assert.notNull(topK, "topK must not be null");
            Assert.notNull(textSeed, "textSeed must not be null");
            Assert.notNull(maxRefineTokens, "maxRefineTokens must not be null");
            Assert.notNull(maxInferTokens, "maxInferTokens must not be null");
            Assert.notNull(textSeed, "textSeed must not be null");
            Assert.notNull(customVoice, "customVoice must not be null");
            Assert.isTrue(speed > 0 && speed <= 9, "speed must be between 1 and 9");
            Assert.isTrue(temperature > 0 && temperature <= 1, "temperature must be between 0 and 1");
            this.text = text;
            this.prompt = prompt;
            this.voice = voice;
            this.speed = speed;
            this.temperature = Objects.requireNonNullElse(temperature, 0.3F);
            this.topP = Objects.requireNonNullElse(topP, 0.7F);
            this.topK = Objects.requireNonNullElse(topK, 20);
            this.maxRefineTokens = maxRefineTokens;
            this.maxInferTokens = maxInferTokens;
            this.textSeed = textSeed;
            this.skipRefine = Objects.requireNonNullElse(skipRefine, 0);
            this.stream = Objects.requireNonNullElse(stream, 0);
            this.customVoice = customVoice;
        }

        /**
         * The voice to use for synthesis.
         */
        public enum Voice {

            VOICE_11("11.csv"),
            VOICE_1111("1111.csv"),
            VOICE_12("12.csv"),
            VOICE_1234("1234.csv"),
            VOICE_13("13.csv"),
            VOICE_14("14.csv"),
            VOICE_16("16.csv"),
            VOICE_2222("2222.csv"),
            VOICE_2279("2279.csv"),
            VOICE_3333("3333.csv"),
            VOICE_4099("4099.csv"),
            VOICE_5("5.csv"),
            VOICE_5099("5099.csv"),
            VOICE_5555("5555.csv"),
            VOICE_6653("6653.csv"),
            VOICE_6666("6666.csv"),
            VOICE_7777("7777.csv"),
            VOICE_7869("7869.csv"),
            VOICE_8888("8888.csv"),
            VOICE_9999("9999.csv"),
            VOICE_SEED_1518_RESTORED_EMB("seed_1518_restored_emb.pt"),
            VOICE_SEED_1983_RESTORED_EMB("seed_1983_restored_emb.pt"),
            ;

            public final String value;

            private Voice(String value) {
                this.value = value;
            }

            public String getValue() {
                return this.value;
            }

        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * Builder for the SpeechRequest.
         */
        public static class Builder {

            private String text;
            private String prompt;
            private String voice;
            private Integer speed = 1;
            private Float temperature = 0.3F;
            private Float topP = 0.7F;
            private Integer topK = 20;
            private Integer maxRefineTokens;
            private Integer maxInferTokens;
            private Float textSeed;
            private Integer skipRefine;
            private Integer stream;
            private Integer customVoice;

            public Builder withText(String text) {
                this.text = text;
                return this;
            }

            public Builder withPrompt(String prompt) {
                this.prompt = prompt;
                return this;
            }

            public Builder withVoice(String voice) {
                this.voice = voice;
                return this;
            }

            public Builder withSpeed(Integer speed) {
                this.speed = speed;
                return this;
            }

            public Builder withTemperature(Float temperature) {
                this.temperature = temperature;
                return this;
            }

            public Builder withTopP(Float topP) {
                this.topP = topP;
                return this;
            }

            public Builder withTopK(Integer topK) {
                this.topK = topK;
                return this;
            }

            public Builder withMaxRefineTokens(Integer maxRefineTokens) {
                this.maxRefineTokens = maxRefineTokens;
                return this;
            }

            public Builder withMaxInferTokens(Integer maxInferTokens) {
                this.maxInferTokens = maxInferTokens;
                return this;
            }

            public Builder withTextSeed(Float textSeed) {
                this.textSeed = textSeed;
                return this;
            }

            public Builder withSkipRefine(Integer skipRefine) {
                this.skipRefine = skipRefine;
                return this;
            }

            public Builder withStream(Integer stream) {
                this.stream = stream;
                return this;
            }

            public Builder withCustomVoice(Integer customVoice) {
                this.customVoice = customVoice;
                return this;
            }

            public SpeechRequest build() {
                return new SpeechRequest(this.text, this.prompt, this.voice, this.speed, this.temperature, this.topP, this.topK,
                        this.maxRefineTokens, this.maxInferTokens, this.textSeed, this.skipRefine, this.stream, this.customVoice);
            }

        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SpeechResponse(
            @JsonProperty("code") Integer code,
            @JsonProperty("msg") String msg,
            @JsonProperty("filename") String filename,
            @JsonProperty("url") String url,
            @JsonProperty("audio_files") List<AudioFile> audioFiles) {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record AudioFile(
                @JsonProperty("audio_duration") Float audioDuration,
                @JsonProperty("filename") String filename,
                @JsonProperty("inference_time") Float inferenceTime,
                @JsonProperty("url") String url) {
        }

    }

    /**
     * Request to generates audio from the input text.
     * @param requestBody The request body.
     * @return Response entity containing the audio SpeechResponse.
     */
    public ResponseEntity<SpeechResponse> createSpeech(SpeechRequest requestBody) {
        return this.restClient.post().uri("/tts").body(requestBody).retrieve().toEntity(SpeechResponse.class);
    }

    /**
     * Streams audio generated from the input text.
     *
     * This method sends a POST request to the OpenAI API to generate audio from the
     * provided text. The audio is streamed back as a Flux of ResponseEntity objects, each
     * containing a byte array of the audio data.
     * @param requestBody The request body containing the details for the audio
     * generation, such as the input text, model, voice, and response format.
     * @return A Flux of ResponseEntity objects, each containing a byte array of the audio
     * data.
     */
    public Flux<ResponseEntity<SpeechResponse>> stream(SpeechRequest requestBody) {

        return webClient.post()
                .uri("/tts")
                .body(Mono.just(requestBody), SpeechRequest.class)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchangeToFlux(clientResponse -> {
                    HttpHeaders headers = clientResponse.headers().asHttpHeaders();
                    return clientResponse.bodyToFlux(SpeechResponse.class)
                            .map(bytes -> ResponseEntity.ok().headers(headers).body(bytes));
                });
    }

}
