package com.api.portfolio_java_services.configuartion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {



    @Bean
    public WebClient ollamaWebClient(@Value("${spring.ai.ollama.base-url}") String ollamaUrl) {
        return WebClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }

    @Bean
    public WebClient netlifyWebClient(@Value("${netlify.api.url}") String netlifyUrl) {
        return WebClient.builder()
                .baseUrl(netlifyUrl)
                .build();
    }
}
