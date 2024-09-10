package com.quizApp.Backend.MainAppClass.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.ResultService;
import com.quizApp.Backend.MainAppClass.model.Result;

import java.util.List;


@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

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
}

