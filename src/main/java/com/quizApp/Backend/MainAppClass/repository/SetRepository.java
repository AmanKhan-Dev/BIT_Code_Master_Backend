package com.quizApp.Backend.MainAppClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizApp.Backend.MainAppClass.model.Question_Set;

public interface SetRepository extends JpaRepository<Question_Set, String> {
    boolean existsByQuestionSetId(String questionSetId);
    
}
