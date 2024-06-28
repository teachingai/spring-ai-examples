package com.github.teachingai.ollama.audio;


import com.github.teachingai.ollama.api.EmotiVoiceApi;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.ResponseMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Audio speech metadata implementation for {@literal OpenAI}.
 *
 * @author Ahmed Yousri
 * @see RateLimit
 */
public class ChatTtsAudioSpeechResponseMetadata implements ResponseMetadata {

    protected static final String AI_METADATA_STRING = "{ @type: %1$s, requestsLimit: %2$s }";

    public static final ChatTtsAudioSpeechResponseMetadata NULL = new ChatTtsAudioSpeechResponseMetadata() {
    };

    public static ChatTtsAudioSpeechResponseMetadata from(EmotiVoiceApi.StructuredResponse result) {
        Assert.notNull(result, "ChatTTS speech must not be null");
        ChatTtsAudioSpeechResponseMetadata speechResponseMetadata = new ChatTtsAudioSpeechResponseMetadata();
        return speechResponseMetadata;
    }

    public static ChatTtsAudioSpeechResponseMetadata from(String result) {
        Assert.notNull(result, "ChatTTS speech must not be null");
        ChatTtsAudioSpeechResponseMetadata speechResponseMetadata = new ChatTtsAudioSpeechResponseMetadata();
        return speechResponseMetadata;
    }

    @Nullable
    private RateLimit rateLimit;

    public ChatTtsAudioSpeechResponseMetadata() {
        this(null);
    }

    public ChatTtsAudioSpeechResponseMetadata(@Nullable RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Nullable
    public RateLimit getRateLimit() {
        RateLimit rateLimit = this.rateLimit;
        return rateLimit != null ? rateLimit : new EmptyRateLimit();
    }

    public ChatTtsAudioSpeechResponseMetadata withRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
        return this;
    }

    @Override
    public String toString() {
        return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
    }

}
