package com.quizApp.Backend.MainAppClass.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.ResultService;
import com.quizApp.Backend.MainAppClass.model.Result;
import com.quizApp.Backend.MainAppClass.repository.ResultRepository;

import java.util.List;


@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/email")
    public ResponseEntity<List<Result>> getResultByEmail(@RequestParam String email) {
        List<Result> results = resultService.getResultByEmail(email);
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }


    @PostMapping("/saveResult")
    public ResponseEntity<Result> saveResult(@RequestBody Result result) {
        Result savedResult = resultService.saveResult(result);
        return new ResponseEntity<>(savedResult, HttpStatus.CREATED);
    }

    @GetMapping("/exists")
    public ResponseEntity<String> checkResultExists(
            @RequestParam String questionSetId,
            @RequestParam int questionNo,
            @RequestParam String email) {
        boolean exists = resultRepository.existsByQuestionSetIdAndQuestionNoAndEmail(questionSetId, questionNo, email);
        if (exists) {
            return new ResponseEntity<>("Yes it exists", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No it does not exist", HttpStatus.NOT_FOUND);
        }
    }
}

