package com.quizApp.Backend.MainAppClass.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompileResult {
        final boolean success;
        final String output;
        
    }
