package com.quizApp.Backend.MainAppClass.controller;

import com.quizApp.Backend.MainAppClass.Service.Question_Adder_Service;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/codingQuestions")
public class Question_Adder_controller {

    @Autowired
    Question_Adder_Service qService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewQuestion(@Valid @RequestBody Question_Adder questionAdder) {
        try {
            qService.insertIfNotExists(questionAdder);
            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding question: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateQuestion(@Valid @RequestBody Question_Adder questionAdder) {
        try {
            qService.updateQuestion(questionAdder);
            return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating question: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{questionSetId}/{questionNo}")
    public ResponseEntity<Question_Adder> getQuestion(@PathVariable String questionSetId, @PathVariable int questionNo) {
        @SuppressWarnings("unchecked")
        Optional<Question_Adder> question = qService.getQuestionBySetIdAndNo(questionSetId, questionNo);
        return question.map(ResponseEntity::ok)
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question_Adder>> getAllQuestions() {
        List<Question_Adder> questions = qService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/questionsBySetId")
    public ResponseEntity<List<Map<String, Object>>> getQuestionsBySetId(@RequestParam String questionSetId) {
        List<Map<String, Object>> questions = qService.getQuestionsBySetId(questionSetId);
        if (!questions.isEmpty()) {
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
