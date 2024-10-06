package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizApp.Backend.MainAppClass.model.Question_Categories;
import com.quizApp.Backend.MainAppClass.model.QuestionCategoriesId;
public interface Question_Categories_repo extends JpaRepository<Question_Categories, QuestionCategoriesId> {
  

}
