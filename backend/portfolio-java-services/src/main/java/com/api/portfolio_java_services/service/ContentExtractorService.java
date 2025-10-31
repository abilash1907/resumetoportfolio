package com.api.portfolio_java_services.service;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentExtractorService {





    public Map<String, String> extractContent(final MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                response.put("error", "File name is missing");
                return response;
            }

            String content;

            if (filename.toLowerCase().endsWith(".pdf")) {
                content = extractTextFromPDF(file.getInputStream());
            } else if (filename.toLowerCase().endsWith(".docx")) {
                content = extractTextFromDocx(file.getInputStream());
            } else {
                response.put("error", "Unsupported file format. Only PDF and DOCX are supported.");
                return response;
            }

            log.info("content got from uploaded file: {}", content);
            response.put("filename", filename);
            response.put("content", content);
            return response;

        } catch (Exception e) {
            response.put("error", "Failed to extract content: " + e.getMessage());
            return response;
        }
    }

    private String extractTextFromPDF(InputStream inputStream) throws Exception {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document).trim();
        }
    }

    private String extractTextFromDocx(InputStream inputStream) throws Exception {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            return document.getParagraphs().stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n")).trim();
        }
    }
}
