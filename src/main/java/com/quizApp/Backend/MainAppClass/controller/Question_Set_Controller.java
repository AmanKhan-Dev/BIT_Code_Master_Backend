package com.quizApp.Backend.MainAppClass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizApp.Backend.MainAppClass.Service.Question_Set_Service;
import com.quizApp.Backend.MainAppClass.model.Question_Set;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sets")
@Validated
public class Question_Set_Controller {

    @Autowired
    private Question_Set_Service setService;

    @PostMapping("/addset")
    public ResponseEntity<String> addNewSet(@Valid @RequestBody Question_Set set) {
        try {
            setService.saveSet(set);
            return new ResponseEntity<>("Question set added successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add question set: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                .findFirst()
                                .orElse("Invalid input");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


  
     @GetMapping("/allsets")
    public ResponseEntity<Iterable<Question_Set>> getAllQuestions(){
        Iterable<Question_Set> question_set = setService.getAllQuestionSets();
        return ResponseEntity.ok(question_set);
    }

}
