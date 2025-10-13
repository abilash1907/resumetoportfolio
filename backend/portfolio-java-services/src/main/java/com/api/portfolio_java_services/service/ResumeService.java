package com.api.portfolio_java_services.service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.resumeparser.model.ResumeModal;
import java.io.IOException;
import java.util.Arrays;

@Service
public class ResumeService {

    public ResumeModal parse(MultipartFile file) {
        ResumeModal resume = new ResumeModal();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // ⚙️ Simple keyword extraction demo
            if (text.contains("React")) resume.setTitle("React Developer");
            resume.setName(extractName(text));
            resume.setSkills(Arrays.asList("React", "Spring Boot", "Java", "MongoDB"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resume;
    }

    private String extractName(String text) {
        // Dummy logic — later you can improve with regex or NLP
        return text.split("\n")[0].trim();
    }
}
