package com.quizApp.Backend.MainAppClass.controller;



import com.quizApp.Backend.MainAppClass.Service.TestCaseService;
import com.quizApp.Backend.MainAppClass.model.TestCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    // Endpoint to add multiple test cases
    @PostMapping("/add")
    public ResponseEntity<List<TestCase>> addMultipleTestCases(@RequestBody List<TestCase> testCases) {
        List<TestCase> addedTestCases = testCaseService.addMultipleTestCases(testCases);
        return new ResponseEntity<>(addedTestCases, HttpStatus.CREATED);
    }

    // Endpoint to get test cases by composite ID
    @GetMapping("/{questionSetId}/{questionNo}")
    public ResponseEntity<List<TestCase>> getTestCasesById(
            @PathVariable String questionSetId,
            @PathVariable int questionNo) {
        List<TestCase> testCases = testCaseService.getTestCasesById(questionSetId, questionNo);
        if (testCases.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(testCases, HttpStatus.OK);
    }
}
