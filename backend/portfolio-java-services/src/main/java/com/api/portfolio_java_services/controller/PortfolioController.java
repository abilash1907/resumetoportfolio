package com.api.portfolio_java_services.controller;

import com.api.portfolio_java_services.model.Request;
import com.api.portfolio_java_services.service.AIService;
import com.api.portfolio_java_services.service.ContentExtractorService;
import com.api.portfolio_java_services.service.NetlifyDeployService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:5173") // for React
public class PortfolioController {

    @Autowired
    private final ContentExtractorService contentExtractorService;
    private final AIService aiService;
    private final NetlifyDeployService netlifyDeployService;




//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/extractContent",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Map<String, String>> classify(@Valid @NotNull @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        Map<String, String> response = new HashMap<>();
        response=contentExtractorService.extractContent(file);
        if(response.containsKey("error")){
            return ResponseEntity.badRequest().body(response);
        } else if (response.containsKey("content")) {
            response.put("jsonContent", aiService.convertToJson(response.get("content")));
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping(value = "/generateWebsiteFromAi",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateWebsiteFromAi(@RequestBody Request request) {
        return aiService.generateWebsiteFromAI(request);
    }

    @PostMapping("/publishWebsite/{siteId}")
    public ResponseEntity<?> publishWebsiteUsingAi(@PathVariable String siteId) {
        try {
            String domain = aiService.generateDomainName(siteId);
            String url = netlifyDeployService.deploySite(siteId, domain);

            return ResponseEntity.ok(Map.of(
                    "status", "Success",
                    "domain", domain,
                    "publicUrl", url
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "Failed",
                    "message", e.getMessage()
            ));
        }
    }
}
