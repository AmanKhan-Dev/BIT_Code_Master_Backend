package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Result;

@Repository
public interface AIReviewRepo extends JpaRepository {
@Query("SELECT r FROM Result r WHERE r.questionSetId = ?1 AND r.questionNo = ?2")
        List<Result> findResultsByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo);
    
} 
