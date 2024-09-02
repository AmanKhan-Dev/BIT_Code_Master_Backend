package com.quizApp.Backend.MainAppClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quizApp.Backend.MainAppClass.model.Question_Adder;

import jakarta.transaction.Transactional;


public interface Question_Adder_Repo extends JpaRepository<Question_Adder, String> {

    @Modifying
@Transactional
@Query(value = "INSERT IGNORE INTO coding_questions (Question_Set_Id, Question_no, Question) VALUES (:questionSetId, :questionNo, :question) ", nativeQuery = true)
void insertIfNotExists(@Param("questionSetId") String questionSetId, @Param("questionNo") int questionNo, @Param("question") String question);


@Modifying
@Transactional
@Query(value = "UPDATE coding_questions SET Question = :question WHERE Question_Set_Id = :questionSetId AND Question_no = :questionNo", nativeQuery = true)
    void updateQuestion(@Param("questionSetId") String questionSetId, @Param("questionNo") int questionNo, @Param("question") String question);


}
