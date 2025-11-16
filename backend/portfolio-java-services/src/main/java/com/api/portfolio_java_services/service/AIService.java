package com.api.portfolio_java_services.service;
import com.api.portfolio_java_services.model.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class AIService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ollama.api.key}")
    private String ollamaToken;


    public AIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://ollama.com")
                .build();

    }

    public Flux<String> generateWebsiteFromAI(Request request) {
        Mono<String> responseMono= webClient.post()
                .uri("/api/chat")
                .header("Authorization", "Bearer " + ollamaToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);

        return responseMono.flatMapMany(response -> {
            try {
                JsonNode root = objectMapper.readTree(response);
                // Extract message.content field
                String htmlContent=root.path("message").path("content").asText().replace("```html", "").replace("```", "");
                String siteId = UUID.randomUUID().toString();  // generate unique ID
                saveGeneratedFiles(siteId,htmlContent);
                return Flux.just(htmlContent+"siteId="+siteId);
            } catch (Exception e) {
                return Flux.error(new RuntimeException("Error parsing JSON", e));
            }
        });
    }

    private void saveGeneratedFiles(String siteId, String htmlContent) throws IOException {
        String outputDir = "../../generated-sites/";
        Path sitePath = Paths.get(outputDir + siteId);
        Files.createDirectories(sitePath);

        // Save HTML
        Path indexFile = sitePath.resolve("index.html");
        Files.writeString(indexFile, htmlContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public String generateDomainName(String siteId)  {
        try{
            // 1️⃣ Read website or resume content
            Path sitePath = Paths.get("../../generated-sites/" + siteId + "/index.html");
            String htmlContent = Files.readString(sitePath);
            log.info("htmlContent="+htmlContent);

            // Create a prompt for the AI
            String prompt = """
            Based on the following portfolio or resume HTML content,
            suggest a short, catchy, professional, and SEO-friendly domain name
            (2-3 words max, lowercase, separated by hyphens).
            Do not include protocols (http:// or https://).
            Return only the domain name (no explanations).

            Example formats:
            - bright-developer
            - creative-coder
            - tech-talent
            - resume-ai-creator

            HTML content:
            """ + htmlContent;
            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-oss:120b-cloud",
                    "messages", List.of(Map.of(
                            "role", "user",
                            "content", prompt
                    )),
                    "stream", false
            );

            // Call the AI model via Spring AI
            String response = webClient.post()
                    .uri("/api/chat")
                    .header("Authorization", "Bearer " + ollamaToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("response="+response);

            // Post-process: attach your deployment base domain
            JsonNode root = objectMapper.readTree(response);

            // Extract message.content field
            String domain=root.path("message").path("content").asText();
            log.info("domain="+domain);
            return domain;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String convertToJson(String content) throws JsonProcessingException {
        String prompt = """
            Analyze the given content getting from resume file and convert this
            content into perfect JSON format. I need only code response from your
            side, no need any other explanation.
            Content :
            """ + content;
        log.info("prompt: {}",prompt);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-oss:120b-cloud",
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", prompt
                )),
                "stream", false
        );
        // Call the AI model via Spring AI
        String response = webClient.post()
                .uri("/api/chat")
                .header("Authorization", "Bearer " + ollamaToken)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("response="+response);

        JsonNode root = objectMapper.readTree(response);
        String jsonContent=root.path("message").path("content").asText().replaceAll("```json|```", "").trim();
        log.info("jsonContent="+jsonContent);
        return jsonContent;
    }

}
