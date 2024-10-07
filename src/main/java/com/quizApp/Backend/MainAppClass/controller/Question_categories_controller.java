package com.quizApp.Backend.MainAppClass.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizApp.Backend.MainAppClass.Service.Question_Categories_service;
import com.quizApp.Backend.MainAppClass.model.Question_Categories;
import com.quizApp.Backend.MainAppClass.repository.Question_Categories_repo;
import com.quizApp.Backend.MainAppClass.model.QuestionCategoriesId;


@RestController
@RequestMapping("/api/categories")
public class Question_categories_controller {

    @Autowired
    private Question_Categories_service questionCategoriesService;



    @Autowired
    private Question_Categories_repo repo;
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody Question_Categories category) {
        return questionCategoriesService.addCategory(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question_Categories>> getAllCategories() {
        return questionCategoriesService.getAllCategories();
    }

   

    @DeleteMapping("/delete/{question_set_id}/{available_category}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable String question_set_id, @PathVariable String available_category) {
        QuestionCategoriesId id = new QuestionCategoriesId(question_set_id, available_category);
        return questionCategoriesService.deleteCategoryById(id);
    }


    @GetMapping("/set/{question_set_id}")
    public ResponseEntity<List<String>> getCategoriesByQuestionSetId(@PathVariable String question_set_id) {
        return questionCategoriesService.getCategoriesByQuestionSetId(question_set_id);
    }

}
