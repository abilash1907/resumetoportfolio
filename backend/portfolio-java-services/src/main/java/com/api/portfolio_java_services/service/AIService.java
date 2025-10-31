package com.api.portfolio_java_services.service;
import com.api.portfolio_java_services.model.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class AIService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AIService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:11434")
                .build();
    }

    public Flux<String> generateWebsiteFromAI(Request request) {

        Mono<String> responseMono= webClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
        return responseMono.flatMapMany(response -> {
            try {
                JsonNode root = objectMapper.readTree(response);
                // Extract message.content field
                return Flux.just(root.path("message").path("content").asText());
            } catch (Exception e) {
                return Flux.error(new RuntimeException("Error parsing JSON", e));
            }
        });
    }
}
