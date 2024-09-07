package com.quizApp.Backend.MainAppClass.repository;

import com.quizApp.Backend.MainAppClass.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, String> {
    List<TestCase> findByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo);
}
