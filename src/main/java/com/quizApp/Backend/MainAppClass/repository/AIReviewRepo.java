package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quizApp.Backend.MainAppClass.model.AIReview;
import com.quizApp.Backend.MainAppClass.model.Result;

public interface AIReviewRepo extends JpaRepository<AIReview, Long> {
        @Query("SELECT r FROM Result r WHERE r.questionSetId = ?1 AND r.questionNo = ?2")
        List<Result> findResultsByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo);
}
