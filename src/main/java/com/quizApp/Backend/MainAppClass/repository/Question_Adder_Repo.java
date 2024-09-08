package com.quizApp.Backend.MainAppClass.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.quizApp.Backend.MainAppClass.model.Question_Adder;
import jakarta.transaction.Transactional;

@Repository
public interface Question_Adder_Repo extends JpaRepository<Question_Adder, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO coding_questions (Question_Set_Id, Question_no, Question, question_description) VALUES (:questionSetId, :questionNo, :question, :question_description,:test_case_input,:test_case_output)", nativeQuery = true)
    void insertIfNotExists(@Param("questionSetId") String questionSetId, 
                           @Param("questionNo") int questionNo, 
                           @Param("question") String question,
                           @Param("question_description") String question_description,
                           @Param("test_case_input") String test_case_input,
                           @Param("test_case_output") String test_case_output);

 
    @Modifying
    @Transactional
    @Query(value = "UPDATE coding_questions SET Question = :question, question_description = :question_description ,test_case_input=:test_case_input,test_case_output = :test_case_output WHERE Question_Set_Id = :questionSetId AND Question_no = :questionNo", nativeQuery = true)
    void updateQuestion(@Param("questionSetId") String questionSetId, 
                        @Param("questionNo") int questionNo, 
                        @Param("question") String question,
                        @Param("question_description") String question_description,
                        @Param("test_case_input") String test_case_input,
                        @Param("test_case_output") String test_case_output
                        );


                        Question_Adder findByQuestionSetIdAndQuestionNo(String questionSetId, int questionNo);

                       
                        @Query(value = "SELECT question_no,question FROM coding_questions WHERE question_set_id = :questionSetId", nativeQuery = true)
                        List<String> findQuestionsBySetId(@Param("questionSetId") String questionSetId);
}
