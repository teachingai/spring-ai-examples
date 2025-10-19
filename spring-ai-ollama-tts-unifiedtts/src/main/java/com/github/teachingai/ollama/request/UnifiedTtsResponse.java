package com.github.teachingai.ollama.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedTtsResponse {

    private boolean success;
    private String message;
    private long timestamp;
    private UnifiedTtsResponseData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnifiedTtsResponseData {
        @JsonProperty("request_id")
        private String requestId;

        @JsonProperty("audio_url")
        private String audioUrl;

        @JsonProperty("file_size")
        private long fileSize;
    }
}