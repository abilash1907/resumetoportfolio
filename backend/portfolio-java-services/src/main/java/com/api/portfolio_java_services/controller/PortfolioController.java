package com.api.portfolio_java_services.controller;

import com.api.portfolio_java_services.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.resumeparser.model.ResumeModal;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // for React
public class PortfolioController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeModal> uploadResume(@RequestParam("file") MultipartFile file) {
        ResumeModal data = resumeService.parse(file);
        return ResponseEntity.ok(data);
    }
}
