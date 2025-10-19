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

            af_ZA_AdriNeural("af-ZA-AdriNeural", "Adri", "af", "Female", "Afrikaans (South Africa)"),

            // 中文语音
            zh_CN_XiaoxiaoNeural("zh-CN-XiaoxiaoNeural", "Xiaoxiao", "zh", "Female", "Chinese (Mainland) - 晓晓"),
            zh_CN_YunxiNeural("zh-CN-YunxiNeural", "Yunxi", "zh", "Male", "Chinese (Mainland) - 云希"),
            zh_CN_YunjianNeural("zh-CN-YunjianNeural", "Yunjian", "zh", "Male", "Chinese (Mainland) - 云健"),
            zh_CN_XiaoyiNeural("zh-CN-XiaoyiNeural", "Xiaoyi", "zh", "Female", "Chinese (Mainland) - 晓伊"),
            zh_CN_YunyangNeural("zh-CN-YunyangNeural", "Yunyang", "zh", "Male", "Chinese (Mainland) - 云扬"),
            zh_TW_HsiaoChenNeural("zh-TW-HsiaoChenNeural", "HsiaoChen", "zh", "Female", "Chinese (Taiwan) - 曉臻"),
            zh_HK_HiuMaanNeural("zh-HK-HiuMaanNeural", "HiuMaan", "zh", "Female", "Chinese (Hong Kong) - 曉曼"),

            // 英语语音
            en_US_AriaNeural("en-US-AriaNeural", "Aria", "en", "Female", "English (United States)"),
            en_US_JennyNeural("en-US-JennyNeural", "Jenny", "en", "Female", "English (United States)"),
            en_US_GuyNeural("en-US-GuyNeural", "Guy", "en", "Male", "English (United States)"),
            en_GB_SoniaNeural("en-GB-SoniaNeural", "Sonia", "en", "Female", "English (United Kingdom)"),
            en_GB_RyanNeural("en-GB-RyanNeural", "Ryan", "en", "Male", "English (United Kingdom)"),
            en_AU_AnnetteNeural("en-AU-AnnetteNeural", "Annette", "en", "Female", "English (Australia)"),
            en_AU_CarlyNeural("en-AU-CarlyNeural", "Carly", "en", "Female", "English (Australia)"),

            // 日语语音
            ja_JP_NanamiNeural("ja-JP-NanamiNeural", "Nanami", "ja", "Female", "Japanese - 七海"),
            ja_JP_KeitaNeural("ja-JP-KeitaNeural", "Keita", "ja", "Male", "Japanese - 慶太"),

            // 韩语语音
            ko_KR_SoonBokNeural("ko-KR-SoonBokNeural", "SoonBok", "ko", "Female", "Korean - 順福"),
            ko_KR_InJoonNeural("ko-KR-InJoonNeural", "InJoon", "ko", "Male", "Korean - 仁俊"),

            // 法语语音
            fr_FR_DeniseNeural("fr-FR-DeniseNeural", "Denise", "fr", "Female", "French (France)"),
            fr_FR_HenriNeural("fr-FR-HenriNeural", "Henri", "fr", "Male", "French (France)"),
            fr_CA_SylvieNeural("fr-CA-SylvieNeural", "Sylvie", "fr", "Female", "French (Canada)"),

            // 德语语音
            de_DE_KatjaNeural("de-DE-KatjaNeural", "Katja", "de", "Female", "German (Germany)"),
            de_DE_ConradNeural("de-DE-ConradNeural", "Conrad", "de", "Male", "German (Germany)"),

            // 西班牙语语音
            es_ES_ElviraNeural("es-ES-ElviraNeural", "Elvira", "es", "Female", "Spanish (Spain)"),
            es_ES_AlvaroNeural("es-ES-AlvaroNeural", "Alvaro", "es", "Male", "Spanish (Spain)"),
            es_MX_DaliaNeural("es-MX-DaliaNeural", "Dalia", "es", "Female", "Spanish (Mexico)"),

            // 意大利语语音
            it_IT_ElsaNeural("it-IT-ElsaNeural", "Elsa", "it", "Female", "Italian (Italy)"),
            it_IT_DiegoNeural("it-IT-DiegoNeural", "Diego", "it", "Male", "Italian (Italy)"),

            // 葡萄牙语语音
            pt_BR_FranciscaNeural("pt-BR-FranciscaNeural", "Francisca", "pt", "Female", "Portuguese (Brazil)"),
            pt_BR_AntonioNeural("pt-BR-AntonioNeural", "Antonio", "pt", "Male", "Portuguese (Brazil)"),
            pt_PT_FernandaNeural("pt-PT-FernandaNeural", "Fernanda", "pt", "Female", "Portuguese (Portugal)"),

            // 俄语语音
            ru_RU_SvetlanaNeural("ru-RU-SvetlanaNeural", "Svetlana", "ru", "Female", "Russian (Russia)"),
            ru_RU_DmitryNeural("ru-RU-DmitryNeural", "Dmitry", "ru", "Male", "Russian (Russia)"),

            // 阿拉伯语语音
            ar_SA_ZariyahNeural("ar-SA-ZariyahNeural", "Zariyah", "ar", "Female", "Arabic (Saudi Arabia)"),
            ar_EG_SalmaNeural("ar-EG-SalmaNeural", "Salma", "ar", "Female", "Arabic (Egypt)"),

            // 印地语语音
            hi_IN_SwaraNeural("hi-IN-SwaraNeural", "Swara", "hi", "Female", "Hindi (India)"),
            hi_IN_MadhurNeural("hi-IN-MadhurNeural", "Madhur", "hi", "Male", "Hindi (India)"),
            ;

            public final String voiceId;
            public final String voiceName;
            public final String language;
            public final String gender;
            public final String description;

            private EdgeTtsVoice(String voiceId, String voiceName, String language, String gender, String description) {
                this.voiceId = voiceId;
                this.voiceName = voiceName;
                this.language = language;
                this.gender = gender;
                this.description = description;
            }
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SpeechModelResponse(
            @JsonProperty("success") Boolean success,
            @JsonProperty("message") String message,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("data") List<String> data) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SpeechModelVoiceResponse(
            @JsonProperty("success") Boolean success,
            @JsonProperty("message") String message,
            @JsonProperty("errorCode") String errorCode,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("data") List<ModelVoice> data) {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record ModelVoice(
                @JsonProperty("voiceId") String voiceId,
                @JsonProperty("voiceName") String voiceName,
                @JsonProperty("gender") String gender,
                @JsonProperty("description") String description) {
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
     * Request to get the list of available models.
     * @return Response entity containing the list of available models.
     */
    public ResponseEntity<SpeechModelResponse> getModels() {
        return this.restClient.get()
                .uri("/tools/models")
                .retrieve()
                .toEntity(SpeechModelResponse.class);
    }

    /**
     * Request to get the list of available models.
     * @return Response entity containing the list of available models.
     */
    public ResponseEntity<SpeechModelVoiceResponse> getModelVoices(String  model) {
        return this.restClient.get()
                .uri(String.format("/tools/voices/%s", model))
                .retrieve()
                .toEntity(SpeechModelVoiceResponse.class);
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
