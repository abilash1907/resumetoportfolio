package com.api.portfolio_java_services.service;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentExtractorService {

    private final ChatClient chatClient;
    public ContentExtractorService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
//    public ExtractModel extractContent(final MultipartFile multipartFile) {
//        String text="";
//        String prompt= """
//                Convert the given text to JSON format.
//                Text:
//                """;
//        Pattern pattern = Pattern.compile("```(.*?)```", Pattern.DOTALL);
//
//        try (final PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
//            final PDFTextStripper pdfStripper = new PDFTextStripper();
//            text = pdfStripper.getText(document);
//
//        } catch (final Exception ex) {
//            text = "Error parsing PDF";
//        }
//        log.info("extract data from file : {}",text);
//        if(text.contains("Error")){
//            extractModel.setStringifyJsonFormat("{}");
//            return extractModel;
//        }
//            prompt=prompt+text;
//            log.info("create prompt for ai : {}",prompt);
////            String aiResponse=chatClient
////                            .prompt()
////                            .user(prompt)
////                            .call()
////                            .content();
//            String aiResponse=input;
//            log.info("aiResponse : {}",aiResponse);
//            Matcher matcher = pattern.matcher(aiResponse==null?"":aiResponse);
//            String jsonContent="{}";
//            if (matcher.find()) {
//                jsonContent = matcher.group(1).trim(); // Content inside triple quotes
//            }
//            log.info("jsonContent : {}",jsonContent);
//            extractModel.setStringifyJsonFormat(jsonContent);
//
//
//        return extractModel;
//    }

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
