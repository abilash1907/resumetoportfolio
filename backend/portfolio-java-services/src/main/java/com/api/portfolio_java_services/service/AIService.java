package com.api.portfolio_java_services.service;

import com.api.portfolio_java_services.model.GenerateWebsiteRequest;
import com.api.portfolio_java_services.model.GenerateWebsiteResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Slf4j
@Service
public class AIService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private final AIClient aiClient;
    @Value("${app.generated-dir}")
    private String directory;

    public AIService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public GenerateWebsiteResponse generateWebsiteFromAI(GenerateWebsiteRequest generateWebsiteRequest) {
        try{
            String prompt= """
            Analyze the given content getting from resume file and create a single-file solution
            for a professional and attractive complete modern personal portfolio website based
            on the provided resume content using HTML,CSS and JS only in single file.
            The design uses a clean layout, modern typography, and a subtle color scheme.
            I need only code response from your side, no need any other explanation.
            Content :
            """ + generateWebsiteRequest.getContent();
            String response=aiClient.ollamaApiService(generateWebsiteRequest.getRole(),prompt);
            String content=response.replace("```html", "").replace("```", "");
            String siteId = UUID.randomUUID().toString();  // generate unique ID
            saveGeneratedFiles(siteId,content);
            GenerateWebsiteResponse generateWebsiteResponse = new GenerateWebsiteResponse();
            generateWebsiteResponse.setSiteId(siteId);
            generateWebsiteResponse.setHtmlContent(content);
            return generateWebsiteResponse;
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveGeneratedFiles(String siteId, String htmlContent) throws IOException {
        Path sitePath = Paths.get(directory + siteId);
        Files.createDirectories(sitePath);
        // Save HTML
        Path indexFile = sitePath.resolve("index.html");
        Files.writeString(indexFile, htmlContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public String generateDomainName(String siteId)  {
        try{
            // 1️⃣ Read website or resume content
            Path sitePath = Paths.get(directory + siteId + "/index.html");
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
            return aiClient.ollamaApiService("user",prompt);
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
        String response=aiClient.ollamaApiService("user",prompt);
        String jsonContent=response.replaceAll("```json|```", "").trim();
        log.info("jsonContent="+jsonContent);
        return jsonContent;
    }
}
