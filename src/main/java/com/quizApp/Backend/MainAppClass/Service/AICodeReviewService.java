package com.quizApp.Backend.MainAppClass.Service;

import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.AICodeReviewResponse;

public @Service
class AICodeReviewService {
    
    // ... (RestTemplate setup)

    /**
     * AI analysis for when the user's code has a compile or runtime error.
     */
    public AICodeReviewResponse getAIDebuggingAnalysis(String problemStatement, String userCode, String errorMessage) {
        // Construct a request for the AI with a prompt focused on debugging the error.
        // ... call AI API ...
        return new AICodeReviewResponse(/* AI's debugging feedback */);
    }

    /**
     * AI analysis for when the code works, but needs logical validation.
     */
    public AICodeReviewResponse getAILogicalAnalysis(String problemStatement, String userCode) {
        // Construct a request for the AI with a prompt to check if the algorithm
        // (e.g., insertion sort) matches the problem statement.
        // ... call AI API ...
        return new AICodeReviewResponse(/* AI's logic validation feedback */);
    }
 {
    
}
}
