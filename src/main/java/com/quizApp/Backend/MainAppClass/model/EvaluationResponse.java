package com.quizApp.Backend.MainAppClass.model;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class EvaluationResponse {
    private boolean success;
    private String output;
    private String aiReview;
    private List<Stage> stages = new ArrayList<>();

    // Getters and Setters for all fields
    
    public void addStage(String name, String status) {
        this.stages.add(new Stage(name, status));
    }

    public void updateLastStage(String status) {
        if (!stages.isEmpty()) {
            stages.get(stages.size() - 1).setStatus(status);
        }
    }
    
    // Stage inner class
    @Data
    @NoArgsConstructor
    public static class Stage {
        private String name;
        private String status; // e.g., "IN_PROGRESS", "SUCCESS", "FAILED"

        public Stage(String name, String status) {
            this.name = name;
            this.status = status;
        }
        // Getters and Setters
    }
}