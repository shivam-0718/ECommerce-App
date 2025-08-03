package org.ecommerce.app.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class AiImageGeneratorService {

    @Autowired
    private ImageModel imageModel;

    public byte[] generateImage(String query) {
        OpenAiImageOptions options = OpenAiImageOptions
                .builder()
                .N(1)
                .width(1024)
                .height(1024)
                .quality("standard")
                .responseFormat("url")
                .model("dall-e-3")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(query, options);
        ImageResponse response = imageModel.call(imagePrompt);

        String imageUrl = response.getResult().getOutput().getUrl();

        //Getting data in byte format.
        try {
            return new URL(imageUrl).openStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
