package com.github.teachingai.openai.controller;

import org.springframework.ai.image.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ImageGenController {

    private final ImageClient imageClient;

    public ImageGenController(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    @PostMapping("/imagegen")
    public String imageGen(@RequestBody ImageGenRequest request) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .withModel("dall-e-3")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(request.prompt(), options);
        ImageResponse response = imageClient.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        return "redirect:" + imageUrl;
    }


}
