package org.ecommerce.app.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    //Implementation of ChatClient bean through OpenAiChatModel
    @Bean
    public ChatClient createChatClientBean(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }

}
