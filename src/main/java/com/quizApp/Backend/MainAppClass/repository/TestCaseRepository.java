package com.quizApp.Backend.MainAppClass.repository;

import com.quizApp.Backend.MainAppClass.model.TestCase;
import com.quizApp.Backend.MainAppClass.model.TestcaseId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, TestcaseId> {
@Query("SELECT t FROM TestCase t WHERE t.questionSetId = :questionSetId AND t.questionNo = :questionNo")
    List<TestCase> findTestCasesByQuestionSetIdAndQuestionNo(
            @Param("questionSetId") String questionSetId,
            @Param("questionNo") int questionNo);

}
