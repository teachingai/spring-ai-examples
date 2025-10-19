package com.github.teachingai.ollama.audio.speech;

import com.github.teachingai.ollama.ChatTtsAudioSpeechOptions;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;

import java.util.Objects;

/**
 * The {@link SpeechPrompt} class represents a request to the OpenAI Text-to-Speech (TTS)
 * API. It contains a list of {@link SpeechMessage} objects, each representing a piece of
 * text to be converted to speech.
 *
 * @since 2026.06.28
 */
public class SpeechPrompt implements ModelRequest<SpeechMessage> {

    private ChatTtsAudioSpeechOptions speechOptions;

    private final SpeechMessage message;

    public SpeechPrompt(String instructions) {
        this(new SpeechMessage(instructions), ChatTtsAudioSpeechOptions.builder().build());
    }

    public SpeechPrompt(String instructions, ChatTtsAudioSpeechOptions speechOptions) {
        this(new SpeechMessage(instructions), speechOptions);
    }

    public SpeechPrompt(SpeechMessage speechMessage) {
        this(speechMessage, ChatTtsAudioSpeechOptions.builder().build());
    }

    public SpeechPrompt(SpeechMessage speechMessage, ChatTtsAudioSpeechOptions speechOptions) {
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
