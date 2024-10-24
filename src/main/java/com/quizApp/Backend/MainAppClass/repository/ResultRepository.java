package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Result;
import com.quizApp.Backend.MainAppClass.model.ResultId;
@Repository
public interface ResultRepository extends JpaRepository<Result,ResultId> {
    List<Result> findByEmail(String email);

  
        boolean existsByQuestionSetIdAndQuestionNoAndEmail(String questionSetId, int questionNo, String email);
    
    


        @Query("SELECT r FROM Result r WHERE r.questionSetId = ?1 AND r.questionNo = ?2")
        List<Result> findResultsByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo);
}
