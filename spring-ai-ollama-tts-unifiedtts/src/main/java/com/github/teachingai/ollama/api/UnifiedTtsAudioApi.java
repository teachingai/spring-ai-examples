package com.github.teachingai.ollama.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class UnifiedTtsAudioApi {

    public static final String DEFAULT_BASE_URL = "https://unifiedtts.com";

    private final RestClient restClient;

    private final WebClient webClient            ;*
     * Create an new audio api.
     */
    public UnifiedTtsAudioApi() {
        this(DEFAULT_BASE_URL, RestClient.builder(), WebClient.builder(), RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
    }

    /**
     * Create an new chat completion api.
     * @param baseUrl api base URL.
     * @param restClientBuilder RestClient builder.
     * @param responseErrorHandler Response error handler.
     */
    public UnifiedTtsAudioApi(String baseUrl,
                              RestClient.Builder restClientBuilder,
                              ResponseErrorHandler responseErrorHandler) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).messageConverters((x) -> {
            x.add(new FormHttpMessageConverter());
        }).defaultStatusHandler(responseErrorHandler).build();
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Create an new chat completion api.
     * @param baseUrl api base URL.
     * @param restClientBuilder RestClient builder.
     * @param webClientBuilder WebClient builder.
     * @param responseErrorHandler Response error handler.
     */
    public UnifiedTtsAudioApi(String baseUrl,
                              RestClient.Builder restClientBuilder,
                              WebClient.Builder webClientBuilder,
                              ResponseErrorHandler responseErrorHandler) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).messageConverters((x) -> {
            x.add(new FormHttpMessageConverter());
        }).defaultStatusHandler(responseErrorHandler).build();
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Request to generates audio from the input text.
     * @param text The input text to convert to speech.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SpeechRequest(
            @JsonProperty("text") String model, // TTS模型名称，可通过模型列表接口获取
            @JsonProperty("text") String text, // 要转换为语音的文本内容（最大支持10,000字符）
            @JsonProperty("voice") String voice, // 音色ID，每个模型下的音色唯一标识
            @JsonProperty("speed") float speed, // 语速倍率，范围0.5-2.0，默认1.0
            @JsonProperty("pitch") float pitch, // 音调倍率，范围0.5-2.0，默认1.0
            @JsonProperty("volume") float volume, // 音量倍率，范围0.5-2.0，默认1.0
            @JsonProperty("format") String format // 输出音频格式，支持mp3、wav等，默认mp3
        ) {

        public SpeechRequest(String model, String text, String voice) {
            this(model, text, voice, null, null, null, null);
        }

        public SpeechRequest(String model, String text, String voice, Double speed, Double pitch,
                             Double volume, String format) {
            this(model, text, voice, speed, pitch, volume, format);
        }

        /**
         * The Model to use for synthesis.
         */
        public enum Model {
            // Edge TTS models (use Azure voices)
            EDGE_TTS("edge-tts", "Edge TTS", "AZURE"),
            
            // Azure TTS models (use Azure voices)
            AZURE_TTS("azure-tts", "Azure TTS", "AZURE"),
            
            // CosyVoice models (use custom voices)
            COSYVOICE_V1("cosyvoice-v1", "CosyVoice V1", "CosyVoice"),
            COSYVOICE_V2("cosyvoice-v2", "CosyVoice V2", "CosyVoice"),
            
            // Sambert models (use custom voices)
            SAMBERT_V1("sambert-v1", "Sambert V1", "Sambert"),
            
            // Speech models (use custom voices)
            SPEECH_2_5_HD_PREVIEW("speech-2.5-hd-preview", "Speech 2.5 HD Preview", "Speech"),
            SPEECH_2_5_TURBO_PREVIEW("speech-2.5-turbo-preview", "Speech 2.5 Turbo Preview", "Speech"),
            SPEECH_02_HD("speech-02-hd", "Speech 02 HD", "Speech"),
            SPEECH_02_TURBO("speech-02-turbo", "Speech 02 Turbo", "Speech"),
            SPEECH_01_HD("speech-01-hd", "Speech 01 HD", "SPEECH"),
            SPEECH_01_TURBO("speech-01-turbo", "Speech 01 Turbo", "Speech"),
            
            // ElevenLabs models (use ElevenLabs voices)
            ELEVEN_FLASH_V2("eleven_flash_v2", "Eleven Flash V2", "ELEVENLABS"),
            ELEVEN_FLASH_V2_5("eleven_flash_v2_5", "Eleven Flash V2.5", "ELEVENLABS"),
            ELEVEN_TURBO_V2("eleven_turbo_v2", "Eleven Turbo V2", "ELEVENLABS"),
            ELEVEN_TURBO_V2_5("eleven_turbo_v2_5", "Eleven Turbo V2.5", "ELEVENLABS"),
            ELEVEN_MULTILINGUAL_V2("eleven_multilingual_v2", "Eleven Multilingual V2", "ELEVENLABS");

            public final String value;
            public final String description;
            public final String voiceType;

            private Model(String value, String description, String voiceType) {
                this.value = value;
                this.description = description;
                this.voiceType = voiceType;
            }

            public String getValue() {
                return this.value;
            }

            public String getDescription() {
                return this.description;
            }

            public String getVoiceType() {
                return this.voiceType;
            }
        }

        /**
         * The voice to use for azure-tts.
         */
        public enum AzureTtsVoice {

        }

        /**
         * The voice to use for sambert-tts.
         */
        public enum SambertTtsVoice {

        }

        /**
         * The voice to use for speech-tts.
         */
        public enum SpeechTtsVoice {

        }

        /**
         * The voice to use for eleven-tts.
         */
        public enum ElevenTtsVoice {

        }
        
        /**
         * The voice to use for edge-tts.
         */
        @Getter
        public enum EdgeTtsVoice {
            // Edge TTS / Azure TTS voices (compatible with edge-tts model)
            // Afrikaans voices
            af_ZA_AdriNeural("af-ZA-AdriNeural", "Female", "Afrikaans (South Africa)"),
            ;

            
            public final String id;
            public final String gender;
            public final String desc;

            private EdgeTtsVoice(String id, String gender, String desc) {
                this.id = id;
                this.gender = gender;
                this.desc = desc;
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
     * @param speechRequest The request body.
     * @return Response entity containing the audio SpeechResponse.
     */
    public ResponseEntity<SpeechResponse> createSpeech(SpeechRequest speechRequest) {

        Assert.notNull(speechRequest, "The request body can not be null.");
        Assert.isTrue(speechRequest.stream() == 0, "Request must set the steam property to 0.");
        MultiValueMap body = ApiUtils.toMultiValueMap(speechRequest);
        return this.restClient.post()
                .uri("/tts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(SpeechResponse.class);
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
