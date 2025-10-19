package com.github.teachingai.ollama;

import io.github.ggerganov.whispercpp.WhisperCpp;
import io.github.ggerganov.whispercpp.params.WhisperSamplingStrategy;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Example {

    public static void main(String[] args) {
        try(WhisperCpp whisper = new WhisperCpp()) {
            ;
            // By default, models are loaded from ~/.cache/whisper/ and are usually named "ggml-${name}.bin"
            // or you can provide the absolute path to the model file.
            whisper.initContext("base.en");
            var whisperParams = whisper.getFullDefaultParams(WhisperSamplingStrategy.WHISPER_SAMPLING_GREEDY);
            // custom configuration if required
            whisperParams.temperature_inc = 0f;

            var samples = readAudio(); // divide each value by 32767.0f
            whisper.fullTranscribe(whisperParams, samples);

            int segmentCount = whisper.benchMemcpy(context);
            for (int i = 0; i < segmentCount; i++) {
                String text = whisper.getTextSegment(context, i);
                System.out.println(segment.getText());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}