package com.api.portfolio_java_services.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;



@Service
public class AIClient {
    private final WebClient ollamaWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${ollama.api.key}")
    private String ollamaToken;
    @Value("${spring.ai.ollama.chat.model}")
    private String aiModel;


    public AIClient(@Qualifier("ollamaWebClient") WebClient ollamaWebClient) {
        this.ollamaWebClient = ollamaWebClient;
    }


    public String ollamaApiService(String role,String prompt) {
        try{
            Map<String, Object> requestBody = Map.of(
                    "model", aiModel,
                    "messages", List.of(Map.of(
                            "role", role,
                            "content", prompt
                    )),
                    "stream", false
            );
            String response= ollamaWebClient.post()
                    .uri("/api/chat")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ollamaToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(response);
            return root.path("message").path("content").asText();
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
