package com.github.teachingai.ollama.audio;


import com.github.teachingai.ollama.api.UnifiedTtsNativeAudioApi;
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
public class UnifiedTtsAudioSpeechResponseMetadata implements ResponseMetadata {

    protected static final String AI_METADATA_STRING = "{ @type: %1$s, requestsLimit: %2$s }";

    public static final UnifiedTtsAudioSpeechResponseMetadata NULL = new UnifiedTtsAudioSpeechResponseMetadata() {
    };

    public static UnifiedTtsAudioSpeechResponseMetadata from(UnifiedTtsNativeAudioApi.SpeechResponse result) {
        Assert.notNull(result, "ChatTTS speech must not be null");
        UnifiedTtsAudioSpeechResponseMetadata speechResponseMetadata = new UnifiedTtsAudioSpeechResponseMetadata();
        return speechResponseMetadata;
    }

    public static UnifiedTtsAudioSpeechResponseMetadata from(String result) {
        Assert.notNull(result, "ChatTTS speech must not be null");
        UnifiedTtsAudioSpeechResponseMetadata speechResponseMetadata = new UnifiedTtsAudioSpeechResponseMetadata();
        return speechResponseMetadata;
    }

    @Nullable
    private RateLimit rateLimit;

    public UnifiedTtsAudioSpeechResponseMetadata() {
        this(null);
    }

    public UnifiedTtsAudioSpeechResponseMetadata(@Nullable RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Nullable
    public RateLimit getRateLimit() {
        RateLimit rateLimit = this.rateLimit;
        return rateLimit != null ? rateLimit : new EmptyRateLimit();
    }

    public UnifiedTtsAudioSpeechResponseMetadata withRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
        return this;
    }

    @Override
    public String toString() {
        return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
    }

}
