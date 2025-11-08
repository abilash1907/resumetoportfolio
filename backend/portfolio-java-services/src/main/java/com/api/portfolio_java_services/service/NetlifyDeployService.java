package com.api.portfolio_java_services.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NetlifyDeployService {

    private final WebClient webClient;





    @Value("${netlify.access.token}")
    private String netlifyToken;

    @Value("${app.generated-dir}")
    private String generatedDir;


    public NetlifyDeployService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.netlify.com/api/v1")
                .build();
    }

    public String deploySite(String siteId, String domainName)  {
        try {
            Path sitePath = Paths.get(generatedDir + "/" + siteId);
            if (!Files.exists(sitePath)) {
                throw new RuntimeException("Site folder not found: " + sitePath);
            }
            Map<String, String> fileMap = new HashMap<>();
            Files.walk(sitePath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            fileMap.put(sitePath.relativize(file).toString().replace("\\", "/"), sha1(file));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

            // Send file manifest to Netlify
            Map<String, Object> requestBody = Map.of(
                    "name", domainName,
                    "draft",false,
                    "files", fileMap
            );

            log.info("requestBody: {}", requestBody);

            Map response = webClient.post()
                    .uri("/sites")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + netlifyToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

//            log.info("netlifyResponse: " + response);
            assert response != null;
            String deploy_id = response.get("deploy_id").toString();
            List<String> requiredFiles = (List<String>) response.get("required");
            uploadFilesToNetlify(sitePath,deploy_id,requiredFiles);
            return "https://" + domainName + ".netlify.app";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void uploadFilesToNetlify(Path sitePath,String deploy_id,List<String> requiredFiles)  {
        try{
            for (String path : requiredFiles) {
                Path fullPath = sitePath.resolve("index.html");
                try {
                    if (Files.isRegularFile(fullPath) && Files.isReadable(fullPath)) {
                        byte[] bytes = Files.readAllBytes(fullPath);
                        webClient.put()
                                .uri("/deploys/" + deploy_id + "/files/" + path)
                                .header("Authorization", "Bearer " + netlifyToken)
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(BodyInserters.fromValue(bytes))
                                .retrieve()
                                .toBodilessEntity()
                                .block();
                        System.out.println("✅ Uploaded: " + path);
                    } else {
                        System.out.println("⚠️ Skipping non-regular or unreadable file: " + path);
                    }
                } catch (AccessDeniedException e) {
                    System.err.println("❌ Access denied for file: " + fullPath);
                } catch (IOException e) {
                    System.err.println("⚠️ Error reading file: " + fullPath + " -> " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String sha1(Path file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] data = Files.readAllBytes(file);
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}



