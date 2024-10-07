package com.quizApp.Backend.MainAppClass.Service;



import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.model.TestcaseId;
import com.quizApp.Backend.MainAppClass.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    // Method to add multiple test cases
    public List<TestCase> addMultipleTestCases(List<TestCase> testCases) {
        return testCaseRepository.saveAll(testCases);
    }

    // Method to get test cases by composite ID
    public List<TestCase> getTestCasesById(String questionSetId, int questionNo) {
        return testCaseRepository.findTestCasesByQuestionSetIdAndQuestionNo(questionSetId, questionNo);
    }
}

