package com.api.portfolio_java_services.controller;

import com.api.portfolio_java_services.service.ContentExtractorService;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/extractContent",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Map<String, String>> classify(@Valid @NotNull @RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        response=contentExtractorService.extractContent(file);
        if(response.containsKey("error")){
            return ResponseEntity.badRequest().body(response);
        } else if (response.containsKey("content")) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(500).body(response);
        }
    }
}
