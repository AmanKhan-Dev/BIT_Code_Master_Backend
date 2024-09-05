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
    @Query(value = "INSERT IGNORE INTO coding_questions (Question_Set_Id, Question_no, Question, question_description) VALUES (:questionSetId, :questionNo, :question, :question_description)", nativeQuery = true)
    void insertIfNotExists(@Param("questionSetId") String questionSetId, 
                           @Param("questionNo") int questionNo, 
                           @Param("question") String question,
                           @Param("question_description") String question_description);

    @Modifying
    @Transactional
    @Query(value = "UPDATE coding_questions SET Question = :question, question_description = :question_description WHERE Question_Set_Id = :questionSetId AND Question_no = :questionNo", nativeQuery = true)
    void updateQuestion(@Param("questionSetId") String questionSetId, 
                        @Param("questionNo") int questionNo, 
                        @Param("question") String question,
                        @Param("question_description") String question_description);



                       
                        @Query(value = "SELECT   question FROM coding_questions WHERE question_set_id = :questionSetId", nativeQuery = true)
                        List<String> findQuestionsBySetId(@Param("questionSetId") String questionSetId);
}
