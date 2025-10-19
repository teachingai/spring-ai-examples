package com.github.teachingai.ollama.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedTtsRequest {
    
    private String model;
    private String voice;
    private String text;
    private Double speed;
    private Double pitch;
    private Double volume;
    private String format;

}