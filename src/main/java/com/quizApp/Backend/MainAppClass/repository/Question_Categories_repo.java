package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quizApp.Backend.MainAppClass.model.Question_Categories;
import com.quizApp.Backend.MainAppClass.model.QuestionCategoriesId;
public interface Question_Categories_repo extends JpaRepository<Question_Categories, QuestionCategoriesId> {
  
@Query("SELECT q FROM Question_Categories q WHERE q.id.question_set_id = :questionSetId")
    List<Question_Categories> findByQuestionSetId(@Param("questionSetId") String questionSetId);
}
