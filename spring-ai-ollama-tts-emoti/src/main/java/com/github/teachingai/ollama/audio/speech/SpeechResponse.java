package com.github.teachingai.ollama.audio.speech;


import com.github.teachingai.ollama.metadata.audio.EmotiVoiceAudioSpeechResponseMetadata;
import org.springframework.ai.model.ModelResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Creates a new instance of SpeechResponse with the given speech result.
 *
 * @since 2024.06.28
 */
public class SpeechResponse implements ModelResponse<Speech> {

    private final Speech speech;

    private final EmotiVoiceAudioSpeechResponseMetadata speechResponseMetadata;

    /**
     * Creates a new instance of SpeechResponse with the given speech result.
     * @param speech the speech result to be set in the SpeechResponse
     * @see Speech
     */
    public SpeechResponse(Speech speech) {
        this(speech, EmotiVoiceAudioSpeechResponseMetadata.NULL);
    }

    /**
     * Creates a new instance of SpeechResponse with the given speech result and speech
     * response metadata.
     * @param speech the speech result to be set in the SpeechResponse
     * @param speechResponseMetadata the speech response metadata to be set in the
     * SpeechResponse
     * @see Speech
     * @see EmotiVoiceAudioSpeechResponseMetadata
     */
    public SpeechResponse(Speech speech, EmotiVoiceAudioSpeechResponseMetadata speechResponseMetadata) {
        this.speech = speech;
        this.speechResponseMetadata = speechResponseMetadata;
    }

    @Override
    public Speech getResult() {
        return speech;
    }

    @Override
    public List<Speech> getResults() {
        return Collections.singletonList(speech);
    }

    @Override
    public EmotiVoiceAudioSpeechResponseMetadata getMetadata() {
        return speechResponseMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeechResponse that)) {
            return false;
        }
        return Objects.equals(speech, that.speech)
                && Objects.equals(speechResponseMetadata, that.speechResponseMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speech, speechResponseMetadata);
    }

}
