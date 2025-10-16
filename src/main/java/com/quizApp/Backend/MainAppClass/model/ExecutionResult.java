package com.quizApp.Backend.MainAppClass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public  class ExecutionResult {
        public final int exitCode;
        public final String output;
        public String errors;
      
    
}
