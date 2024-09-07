package com.quizApp.Backend.MainAppClass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequest {
    private String sourceCode;
    private String language; 
    private String userInput;
    private String questionSetId;
    private Integer questionNo;

    
}
