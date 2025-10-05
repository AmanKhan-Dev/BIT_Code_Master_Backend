package com.quizApp.Backend.MainAppClass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AICodeReviewRequest {
    private String questionSet;
    private int questionNo;
    private String userCode;
    private String prompt;
}