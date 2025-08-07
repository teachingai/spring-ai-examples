package com.github.teachingai.stabilityai.controller;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final StabilityAiImageClient chatModel;

    @Autowired
    public ChatController(StabilityAiImageClient chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/v1/generate")
    public ImageResponse generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        ImagePrompt imagePrompt = new ImagePrompt(message);
        return chatModel.call(imagePrompt);
    }

}
