package com.github.teachingai.ollama.audio.speech;

import com.github.teachingai.ollama.UnifiedTtsAudioSpeechOptions;
import com.github.teachingai.ollama.api.UnifiedTtsAudioApi;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;

import java.util.Objects;

/**
 * The {@link SpeechPrompt} class represents a request to the UnifiedTTS Text-to-Speech (TTS)
 * API. It contains a list of {@link SpeechMessage} objects, each representing a piece of
 * text to be converted to speech.
 */
public class SpeechPrompt implements ModelRequest<SpeechMessage> {

    private UnifiedTtsAudioSpeechOptions speechOptions;

    private final SpeechMessage message;

    public SpeechPrompt(String instructions) {
        this(new SpeechMessage(instructions), UnifiedTtsAudioSpeechOptions.builder()
                .voice(UnifiedTtsAudioApi.DEFAULT_VOICE)
                .build());
    }

    public SpeechPrompt(String instructions, UnifiedTtsAudioSpeechOptions speechOptions) {
        this(new SpeechMessage(instructions), speechOptions);
    }

    public SpeechPrompt(SpeechMessage speechMessage) {
        this(speechMessage, UnifiedTtsAudioSpeechOptions.builder()
                .voice(UnifiedTtsAudioApi.DEFAULT_VOICE)
                .build());
    }

    public SpeechPrompt(SpeechMessage speechMessage, UnifiedTtsAudioSpeechOptions speechOptions) {
        this.message = speechMessage;
        this.speechOptions = speechOptions;
    }

    @Override
    public SpeechMessage getInstructions() {
        return this.message;
    }

    @Override
    public ModelOptions getOptions() {
        return speechOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeechPrompt that)) {
            return false;
        }
        return Objects.equals(speechOptions, that.speechOptions) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speechOptions, message);
    }

}
