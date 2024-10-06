package com.quizApp.Backend.MainAppClass.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizApp.Backend.MainAppClass.model.Question_Categories;
import com.quizApp.Backend.MainAppClass.model.QuestionCategoriesId;

import com.quizApp.Backend.MainAppClass.repository.Question_Categories_repo;

@Service
public class Question_Categories_service {

    @Autowired
    private Question_Categories_repo questionCategoriesRepo;

    public ResponseEntity<String> addCategory(Question_Categories category) {
        // Check if the category already exists
        if (questionCategoriesRepo.existsById(category.getId())) {
            return new ResponseEntity<>("Category with this ID already exists!", HttpStatus.CONFLICT);
        }

        questionCategoriesRepo.save(category);
        return new ResponseEntity<>("Category added successfully!", HttpStatus.OK);
    }

    public ResponseEntity<List<Question_Categories>> getAllCategories() {
        try {
            List<Question_Categories> categories = questionCategoriesRepo.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  

    

    public ResponseEntity<String> deleteCategoryById(QuestionCategoriesId id) {
        try {
            if (questionCategoriesRepo.existsById(id)) {
                questionCategoriesRepo.deleteById(id);
                return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



   
}
