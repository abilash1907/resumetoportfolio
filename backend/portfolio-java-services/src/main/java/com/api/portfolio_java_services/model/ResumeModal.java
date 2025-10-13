package com.example.resumeparser.model;

import lombok.Data;
import java.util.List;

@Data
public class ResumeModal {
    private String name;
    private String title;
    private List<String> skills;
    private List<Project> projects;
    private List<Experience> experience;

    @Data
    public static class Project {
        private String title;
        private String description;
    }

    @Data
    public static class Experience {
        private String company;
        private String role;
        private String years;
    }
}
