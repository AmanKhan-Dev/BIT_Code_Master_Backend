package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;
import com.quizApp.Backend.MainAppClass.model.QuestionKey;
import jakarta.transaction.Transactional;

@Repository
public interface Question_Adder_Repo extends JpaRepository<Question_Adder, QuestionKey> {

    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO coding_questions (Question_Set_Id, Question_no, Question, question_description, test_case_input, test_case_output, question_category) VALUES (:questionSetId, :questionNo, :question, :question_description, :test_case_input, :test_case_output, :question_category)", nativeQuery = true)
    void insertIfNotExists(@Param("questionSetId") String questionSetId, 
                           @Param("questionNo") int questionNo, 
                           @Param("question") String question,
                           @Param("question_description") String question_description,
                           @Param("test_case_input") String test_case_input,
                           @Param("test_case_output") String test_case_output,
                           @Param("question_category") String question_category);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE coding_questions SET Question = :question, question_description = :question_description, test_case_input = :test_case_input, test_case_output = :test_case_output, question_category = :question_category WHERE Question_Set_Id = :questionSetId AND Question_no = :questionNo", nativeQuery = true)
    void updateQuestion(@Param("questionSetId") String questionSetId, 
                        @Param("questionNo") int questionNo, 
                        @Param("question") String question,
                        @Param("question_description") String question_description,
                        @Param("test_case_input") String test_case_input,
                        @Param("test_case_output") String test_case_output,
                        @Param("question_category") String question_category);

    Optional findById(QuestionKey id); // Change method to use composite key

    @Query(value = "SELECT question_no, question, question_category FROM coding_questions WHERE question_set_id = :questionSetId", nativeQuery = true)
    List<Object[]> findQuestionsBySetId(@Param("questionSetId") String questionSetId);
}
